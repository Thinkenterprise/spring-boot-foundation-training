# CLAUDE.md

Leitfaden für Claude Code in diesem Repository.

## Was ist das?

Ein **Spring-Boot-Schulungs-Repository** (Maven). Es bündelt mehrere voneinander
unabhängige Trainingsprojekte, gegliedert in Kapitel `s0` … `s9`. **Es gibt kein
Root-Aggregator-`pom.xml`** – jedes Projekt ist ein **eigenständiges Maven-Projekt**
mit eigenem `./mvnw` / `./mvnw.cmd`.

## Projekt-Konventionen (wichtig!)

Pro Kapitel gibt es i. d. R. mehrere Projektvarianten:

- **`*-final` = Referenz-/Lösungsprojekte.** Diese **müssen immer** kompilieren, ihre
  **Tests müssen grün** sein und die **Anwendung muss starten und über ihren Endpunkt
  erreichbar** sein. Ein Bruch in einem `-final` ist ein **echter Befund**.
- **`*-start` = Teilnehmer-Startprojekte** für die Übungen – der Ausgangspunkt, von dem
  aus Teilnehmer das `-final` erarbeiten. Sie dürfen **absichtlich unvollständig oder
  nicht kompilierbar sein** (auskommentierte Dependencies, `package-info`-Platzhalter,
  leere `main`, Entities ohne passenden Starter). Ein Build-Fehler im `-start` ist
  **per se kein Defekt** – nur dann ein Befund, wenn **vorgegebenes Gerüst** (nicht die
  Teilnehmer-Aufgabe) bricht. Im Zweifel gegen `master` prüfen, ob der Fehler vorbestand.
- **Varianten** wie `*-final-tomcat` (WAR/externer Tomcat) und `*-application-startup`
  (Demo) sind wie `-final` zu behandeln: müssen laufen.
- **`s8-spring-boot-foundation-messaging` ist ein Sonderfall:** bewusst auf
  **Spring Boot 2.7.2** belassen (nicht Teil der SB4-Linie), braucht **RabbitMQ**.
  Separat/vorsichtig bewerten, nicht mit den SB4-Modulen vermengen.

Alle übrigen Module laufen auf **der aktuellsten Spring Boot Version**.

## Build / Run / Test

- Immer den **projekteigenen Maven-Wrapper** nutzen (`./mvnw` bzw. `./mvnw.cmd`).
- **Mindestens JDK 21** erforderlich (die Projekte zielen auf Java 21).
- Build inkl. Tests: `./mvnw -q clean package`. Anwendung starten: `java -jar target/<artefakt>`.
- Apps laufen auf **Port 8080** – sequenziell prüfen, Java-Prozess nach dem Check beenden.
- Der Testcontainer-Test `RouteRepositoryTestcontainerTest` (s6) **benötigt Docker**. Ist Docker
  nicht verfügbar, lässt er sich mit `-Dtest=!RouteRepositoryTestcontainerTest` ausschließen –
  den Nutzer darauf hinweisen. Ein deswegen nicht ausführbarer Test ist **kein K.-o.-Kriterium**.

## Dependencies / Dependabot

- **Dependabot** (`.github/dependabot.yml`, maven, weekly; ignoriert spring-boot-Major-Updates)
  erstellt Update-PRs (`dependabot/maven/...`). Es **pausiert nach Inaktivität** – Reaktivierung
  über GitHub *Insights → Dependency graph → Dependabot → „Check for updates"*.
- Zum **Prüfen von Update-/Dependabot-PRs** gibt es den Skill **`review-dependency-prs`**
  (`.claude/skills/review-dependency-prs/`). Er kennt die Endpunkt-/Test-Matrix je Modul
  und den vollständigen Verifikations-Workflow. Bei Aufträgen wie „prüfe die neuesten PRs/
  Updates/Dependabot-PRs" diesen Skill verwenden.

## Arbeitsregeln

- **Immer nachfragen** vor `merge`, `push`, `--force`, Branch-Löschen oder anderen
  Remote-/Außenwirkungen. Eine Freigabe gilt nur für den konkreten Schritt.
- Working Tree nach Aktionen sauber hinterlassen; Java-Prozesse nach App-Checks beenden.
