---
name: review-dependency-prs
description: >-
  Prüft und verifiziert Dependency-/Dependabot-Update-PRs (oder manuelle
  Versions-Bumps) in diesem Spring-Boot-Trainings-Repo. Auslösen, wenn der
  Nutzer sinngemäß sagt: "prüfe die neuesten PRs / Updates / Dependabot-PRs",
  "schau dir die offenen PRs an", "läuft das Update noch?", "kann ich das
  Dependency-Update mergen?". Der Skill kennt die Trainingsprojekt-Struktur
  (-final = Lösungsprojekte, müssen IMMER bauen/testen/laufen; -start =
  Teilnehmer-Startprojekte, die bewusst unvollständig/fehlerhaft sein dürfen),
  die je Modul zu prüfenden Endpunkte sowie den Build-/Test-/Run-Workflow.
  Fragt IMMER nach, bevor etwas gemergt/gepusht/gelöscht wird.
---

# Dependency-/Dependabot-PRs prüfen

Dieser Skill kapselt das Wissen über dieses Repository und den Verifikations-Workflow,
den wir bei der Spring-Boot-4-Migration etabliert haben. Ziel: Update-PRs eigenständig
fachlich bewerten und dem Nutzer eine klare Merge-Empfehlung geben – **ohne je
ungefragt zu mergen**.

## Was für ein Projekt ist das?

- **Maven-Multi-Projekt-Trainingsrepo** mit Kapiteln `s0` … `s9`. **Kein** Root-Aggregator-`pom.xml`:
  jedes Projekt (`*-final`, `*-start`, Varianten) ist ein **eigenständiges Maven-Projekt**
  mit eigenem `./mvnw` / `./mvnw.cmd`.
- **`-final` = Referenz-/Lösungsprojekte.** Diese **müssen immer** kompilieren, ihre **Tests
  müssen grün** sein und die **Anwendung muss starten und über ihren Endpunkt erreichbar** sein.
  Ein Update darf hier **nichts** kaputt machen. Bricht ein `-final`, ist das ein **echter Befund**.
- **`-start` = Teilnehmer-Startprojekte** für die Übungen. Sie sind der **Ausgangspunkt**, von dem
  aus Teilnehmer das `-final` erarbeiten. Sie dürfen **absichtlich unvollständig sein** (auskommentierte
  Dependencies, `package-info`-Platzhalter, leere `main`, Entities ohne passenden Starter) und
  **kompilieren teils bewusst nicht**. Ein Build-Fehler im `-start` ist **per se kein Defekt** –
  er ist nur dann ein Befund, wenn ein Update **vorgegebenes Gerüst** (nicht die Teilnehmer-Aufgabe)
  bricht. Im Zweifel: gegen `master` vergleichen, ob der Fehler **vorbestand** oder **neu** durch das Update ist.
- **Varianten** wie `-final-tomcat` (WAR/externer Tomcat) und `-application-startup` (Demo) sind wie
  `-final` zu behandeln: müssen laufen.
- **`s8-spring-boot-foundation-messaging` ist ein Sonderfall**: bewusst auf **Spring Boot 2.7.2**
  belassen (nicht Teil der SB4-Linie), braucht **RabbitMQ**. Updates hier separat/vorsichtig bewerten;
  nicht mit den SB4-Modulen über einen Kamm scheren.

## Dependabot

- Konfiguriert in `.github/dependabot.yml` (Ökosystem **maven**, **weekly**; **ignoriert
  spring-boot-Major-Updates**). Erzeugt PRs mit Branch-Präfix `dependabot/maven/...`.
- Dependabot **pausiert automatisch**, wenn seine PRs länger nicht angefasst werden.
  Reaktivieren: GitHub → **Insights → Dependency graph → Reiter „Dependabot" → „Check for updates"**
  (direkt: `https://github.com/Thinkenterprise/spring-boot-foundation-training/network/updates`),
  bzw. **Settings → Code security → Dependabot version updates → Enable**. Künftig PRs zeitnah
  mergen/schließen, damit es aktiv bleibt.

## Umgebung / Werkzeuge

- `gh` CLI für PR-Operationen nutzen. Falls nicht im PATH der laufenden Shell, den
  Installationspfad ermitteln und direkt aufrufen.
- **Mindestens JDK 21** erforderlich (die Projekte zielen auf Java 21). Build/Run/Tests immer
  über den **projekteigenen Maven-Wrapper** (`./mvnw` bzw. `./mvnw.cmd`). Das mitgelieferte
  `verify-app.ps1` ist PowerShell-basiert.
- Der **Testcontainer-Test** `RouteRepositoryTestcontainerTest` (s6) **benötigt Docker**. Fehlt
  Docker, mit `-Dtest=!RouteRepositoryTestcontainerTest` ausschließen und **den Nutzer darauf
  hinweisen** – ein deswegen nicht ausführbarer Test ist **kein K.-o.-Kriterium** (kein Update-Defekt).
- Apps laufen auf **Port 8080** – **sequenziell** prüfen und den Java-Prozess nach jedem Check **beenden**.

## Endpunkte & Testlage je Modul (Prüf-Matrix)

| Modul (`-final`) | Tests | App-Checks (Status / erwartet) |
|---|---|---|
| `s2-…-basics-final` | – | `GET /` → 200, HTML „Spring Boot Training - Hello World" |
| `s2-…-basics-final-tomcat` (WAR) | – | `GET /` → 200, „Hallo World" |
| `s3-…-test-final` | ✅ | `GET /` → 200 · `GET /helloWorld` → „Hello World" |
| `s4-…-configuration-final` | ✅ | Profil `test` (in `application.properties`) · `GET /routes` → 200 (JSON) · `GET /actuator/health` → 200 |
| `s5-…-actuator-final` | – | Actuator-Base-Path **`/thinkenterprise`** · `GET /thinkenterprise` → 200 · `GET /thinkenterprise/health` **toggelt UP (200) ↔ DOWN (503)** über einen `@Scheduled`-Custom-Indicator `routeService` – **503 ist KEIN Fehler**; mehrfach pollen, um beide Zustände zu sehen |
| `s5-…-actuator-application-startup` | – | `GET /` → 200 (läuft auf **Jetty**) |
| `s6-…-data-final` | ✅ (22) | **kein** Web-Controller · `GET /h2-console` → 200 · Testcontainer-Test ausschließen, s. o. |
| `s7-…-rest-final` | ✅ (8) | `GET /routes/101` → 200 · `GET /routes/110000` → **400** (RFC-7807 ProblemDetail) |
| `s9-…-security-final` | ✅ (2) | `GET /routes` ohne Auth → **401** · mit Basic `user:password` → 200 (JSON) |
| `s8-…-messaging-final` | (Sonderfall) | SB 2.7.2, braucht RabbitMQ – separat bewerten |

Endpunkte/Erwartungen ggf. am Code gegenprüfen (Controller-Mappings, `application.properties`/`.yml`,
`SecurityConfiguration`), falls sich etwas geändert hat – nicht blind auf diese Tabelle verlassen.

## Workflow: Update-PRs prüfen

1. **PRs auflisten.**
   `gh pr list --state open --json number,title,headRefName,createdAt`
   - Keine offenen PRs? → Hinweis geben, dass Dependabot evtl. pausiert ist (s. o.), und fragen,
     ob der Nutzer es manuell anstoßen will. (Manuelles Triggern geht nur über die GitHub-UI.)
2. **Pro PR analysieren, was sich wo ändert.**
   `gh pr diff <n>` bzw. `gh pr view <n> --json files` → betroffene **Module** bestimmen
   (welche `pom.xml`/welcher Code?). Notieren: alte → neue Version, betroffene Projekte.
3. **PR auschecken.** `gh pr checkout <n>` (sauberer Working Tree vorausgesetzt).
4. **Betroffene `-final` (+ `-final-tomcat`, `-application-startup`) verifizieren:**
   Build **inkl. Tests** und **App-Start + Endpunkt-Check**. Dafür das mitgelieferte Skript nutzen:
   `& "$PSScriptRoot\verify-app.ps1" -ModuleDir <pfad> -Checks @("URL;;AUTH;;LABEL", ...)`
   (Pfad zum Skript: `.claude\skills\review-dependency-prs\verify-app.ps1`).
   - s6: `-ExtraMvn "-Dtest=!RouteRepositoryTestcontainerTest"` setzen, falls kein Docker.
   - Module ohne Web-Endpunkt nur bauen/testen.
5. **Betroffene `-start` bewerten:** `./mvnw -q clean compile` (bzw. `test-compile`). Ergebnis
   **klassifizieren**: kompiliert = ok; erwarteter Fehler (Teilnehmer-Lücke, z. B. s6 `jakarta.persistence`
   ohne JPA-Starter, s2 auskommentierter Parent) = ok; **unerwarteter** neuer Fehler im vorgegebenen
   Gerüst = Befund. Im Zweifel `git diff origin/master -- <start-pfad>` prüfen.
6. **Zurückmelden** – kompakt und ehrlich:
   - Was wurde aktualisiert (Dependency, alt→neu), welche Projekte betroffen.
   - Pro Projekt: Build/Tests grün? App erreichbar (mit Status-Codes)?
   - Echte Befunde **klar von** Umgebungs-Limits (Docker) und **gewollten `-start`-Lücken trennen**.
7. **Merge-Empfehlung + Rückfrage.** Wenn alles passt: sagen, dass gemergt werden **kann**, und
   **ausdrücklich fragen, ob gemergt werden soll**. **NIEMALS ungefragt mergen.**
8. **Nach bestätigtem Merge:** mit `gh pr merge <n>` mergen (Methode erfragen, falls relevant),
   dann aufräumen: `git checkout master && git pull --prune`, lokalen PR-Branch löschen.

## Feste Regeln

- **Immer fragen** vor `merge`, `push`, `--force`, Branch-Löschen oder anderen Remote-/Außenwirkungen.
  Eine Freigabe gilt nur für den konkreten Schritt, nicht pauschal.
- `-final`-Bruch = echter Befund. `-start`-Bruch erst nach Abgleich (vorbestehend/gewollt vs. neu) bewerten.
- Docker-loser Testcontainer-Fehler und gewollte `-start`-Lücken sind **keine** Update-Regressionen.
- Java-Prozesse nach jedem App-Check beenden; Port 8080 sequenziell nutzen; Working Tree sauber hinterlassen.
