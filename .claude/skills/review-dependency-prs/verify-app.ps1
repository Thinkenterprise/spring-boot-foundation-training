<#
.SYNOPSIS
  Baut ein Maven-Projekt (inkl. Tests), startet das Artefakt (jar/war),
  pollt den Port, ruft definierte Endpunkte ab und beendet den Prozess wieder.

.DESCRIPTION
  Verifikations-Helfer für die Spring-Boot-Trainingsprojekte. Wird vom Skill
  "review-dependency-prs" genutzt, kann aber auch direkt aufgerufen werden.

.EXAMPLE
  ./verify-app.ps1 -ModuleDir "...\s9-...-security-final" `
      -Checks @("http://localhost:8080/routes;;;;noauth-erw-401",
                "http://localhost:8080/routes;;user:password;;auth-erw-200")

.NOTES
  Check-Format pro Eintrag:  "URL;;AUTH;;LABEL"
    - AUTH optional als "user:pass" (HTTP Basic). Leer lassen -> kein Auth.
    - LABEL optional (nur fuer die Ausgabe).
  Nicht-2xx-Antworten (401/400/503 ...) sind KEIN Skriptfehler -> -SkipHttpErrorCheck.
#>
param(
  [Parameter(Mandatory=$true)][string]$ModuleDir,
  [string]$ExtraMvn = "",            # z.B. "-Dtest=!RouteRepositoryTestcontainerTest"
  [switch]$SkipTests,
  [string[]]$Checks = @(),
  [int]$Port = 8080,
  [string]$ReadyUrl = "",            # Default: erste Check-URL
  [int]$ReadyTimeoutSec = 90
)

$ErrorActionPreference = "Continue"
Set-Location $ModuleDir
Write-Output "########## MODULE: $ModuleDir ##########"

# --- Build (+ Tests, sofern nicht uebersprungen) ---
$mvnArgs = @("-q","clean","package")
if ($SkipTests) { $mvnArgs += "-DskipTests" }
if ($ExtraMvn)  { $mvnArgs += $ExtraMvn.Split(" ") }
Write-Output ">>> mvnw $($mvnArgs -join ' ')"
$buildLog = & ".\mvnw.cmd" @mvnArgs 2>&1
$buildExit = $LASTEXITCODE
$buildLog | Select-String -Pattern "Tests run:|BUILD SUCCESS|BUILD FAILURE|ERROR|cannot find symbol|does not exist|Caused by" |
  Select-Object -First 25 | ForEach-Object { Write-Output ("    " + $_.ToString().Trim()) }
Write-Output ">>> BUILD EXIT: $buildExit"
if ($buildExit -ne 0) { Write-Output "!!! BUILD FAILED for $ModuleDir"; return }

if ($Checks.Count -eq 0) { Write-Output "(keine Laufzeit-Checks angefordert)"; return }

# --- Artefakt finden (jar/war, *.original ausschliessen) ---
$art = Get-ChildItem "target\*.jar","target\*.war" -ErrorAction SilentlyContinue |
       Where-Object { $_.Name -notlike "*.original" } | Select-Object -First 1
if (-not $art) { Write-Output "!!! Kein Artefakt in target/"; return }
Write-Output ">>> Starte: $($art.Name)"

$runLog = Join-Path $ModuleDir "run-verify.log"
Remove-Item $runLog,"$runLog.err" -Force -ErrorAction SilentlyContinue
$proc = Start-Process -FilePath "java" -ArgumentList @("-jar",$art.FullName) -PassThru `
          -RedirectStandardOutput $runLog -RedirectStandardError "$runLog.err" -WindowStyle Hidden

function Read-Body($resp) {
  try {
    if ($resp.RawContentStream) {
      $s = [Text.Encoding]::UTF8.GetString($resp.RawContentStream.ToArray())
    } else { $s = [string]$resp.Content }
  } catch { $s = [string]$resp.Content }
  $s = ($s -replace '\s+',' ').Trim()
  if ($s.Length -gt 200) { $s = $s.Substring(0,[Math]::Min(200,$s.Length)) + "..." }
  return $s
}

try {
  if (-not $ReadyUrl) { $ReadyUrl = ($Checks[0] -split ";;")[0] }
  $up = $false
  for ($i=0; $i -lt $ReadyTimeoutSec; $i++) {
    Start-Sleep -Seconds 1
    if ($proc.HasExited) { Write-Output "!!! Prozess früh beendet (Code $($proc.ExitCode))"; break }
    try { Invoke-WebRequest -Uri $ReadyUrl -TimeoutSec 3 -SkipHttpErrorCheck -ErrorAction Stop | Out-Null; $up=$true; break } catch {}
  }
  if (-not $up) {
    Write-Output "!!! App wurde nicht rechtzeitig bereit. Letzte 30 Logzeilen:"
    if (Test-Path $runLog) { Get-Content $runLog -Tail 30 }
    if (Test-Path "$runLog.err") { Get-Content "$runLog.err" -Tail 15 }
    return
  }
  Write-Output ">>> App ist oben nach ~$($i+1)s. Checks:"
  foreach ($c in $Checks) {
    $parts = $c -split ";;"
    $url = $parts[0]
    $auth  = if ($parts.Count -gt 1) { $parts[1] } else { "" }
    $label = if ($parts.Count -gt 2 -and $parts[2]) { $parts[2] } else { $url }
    $h = @{}
    if ($auth) { $h["Authorization"] = "Basic " + [Convert]::ToBase64String([Text.Encoding]::ASCII.GetBytes($auth)) }
    try {
      $resp = Invoke-WebRequest -Uri $url -Headers $h -TimeoutSec 8 -SkipHttpErrorCheck -ErrorAction Stop
      Write-Output ("    [{0}] {1} -> HTTP {2} | {3}" -f $label, $url, $resp.StatusCode, (Read-Body $resp))
    } catch {
      Write-Output ("    [{0}] {1} -> FEHLER: {2}" -f $label, $url, $_.Exception.Message)
    }
  }
} finally {
  if (-not $proc.HasExited) { Stop-Process -Id $proc.Id -Force -ErrorAction SilentlyContinue }
  Remove-Item $runLog,"$runLog.err" -Force -ErrorAction SilentlyContinue
  Write-Output ">>> App gestoppt (pid $($proc.Id))"
}
