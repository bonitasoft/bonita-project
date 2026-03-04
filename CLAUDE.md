# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

Bonita Project provides the **parent POM** and **assembly descriptors** used by Bonita Projects (BPM platform) since Bonita 9.0. It is not an application codebase — it's a Maven build infrastructure project.

## Build Commands

```bash
# Build (without integration tests)
./mvnw verify

# Build with integration tests
./mvnw verify -Ptests

# CI build (what GitHub Actions runs)
./mvnw --no-transfer-progress -B verify -Ptests

# Apply code formatting fixes
./mvnw spotless:apply

# Check formatting without fixing
./mvnw spotless:check

# Build offline (skips Spotless checks)
./mvnw verify -Poffline

# Run a single integration test
./mvnw invoker:run -pl tests -Dinvoker.test=<test-name>
# e.g.: ./mvnw invoker:run -pl tests -Dinvoker.test=application-archive
```

Requires **Java 17** (Temurin) and **Maven 3.9.6+**. Use the Maven wrapper (`./mvnw`).

## Module Structure

- **Root POM** (`pom.xml`) — Aggregator. Manages plugin versions and Spotless config. Modules: `parent`, `assemblies`. The `tests` module is only active with `-Ptests`.
- **`parent/`** — The actual parent POM consumed by downstream Bonita Projects. Defines dependency management (Bonita Runtime BOM, Groovy, Bonita Maven Plugin), plugin management, and build lifecycle including Groovy script executions for project initialization, assembly generation, and Docker resource generation.
- **`assemblies/`** — Assembly descriptors (`src/main/resources/assemblies/`) for packaging Bonita artifacts: application archives, BDM descriptors, Tomcat bundles (community & SP), and classpath dependencies. Also contains Docker resources (`src/main/resources/docker/`).
- **`tests/`** — Maven Invoker integration tests (`src/it/`). Each subdirectory is a self-contained Maven project with `setup` (pre-build) and `verify` (post-build) Groovy hook scripts.

## Code Formatting

Enforced by **Spotless Maven Plugin** during the `process-sources` phase:
- **Java**: Eclipse formatter (`formatter.xml`, 120 char line width, 4-space indent) with GPL-v2.0 license header (`header.txt`)
- **POM files**: sortPom with `recommended_2008_06` ordering, 2-space indent, no blank lines preserved

The build will fail on formatting violations. Run `./mvnw spotless:apply` to auto-fix.

## Maven Profiles

| Profile | Purpose |
|---------|---------|
| `tests` | Enables the `tests` module with Maven Invoker integration tests |
| `release` | GPG artifact signing |
| `central` | Publish to Maven Central via Sonatype Central Publishing Plugin |
| `bundle` | Package as Tomcat bundle (sets `bonita.includeDependencyJars=false`) |
| `docker` | Package as Docker image via `exec-maven-plugin` calling `docker build` |
| `offline` | Skips Spotless formatting checks |

## Integration Tests

Located in `tests/src/it/`. Each test project:
- Has its own `pom.xml` using the parent POM from this project
- Uses `setup.groovy` for pre-build initialization and `verify.groovy` for post-build assertions
- Runs via Maven Invoker Plugin with a local repository at `tests/target/local-repo`
- Test settings in `tests/src/it/settings.xml`

Test projects: `application-archive`, `bdm-descriptor-archive`, `bonita-project-properties`, `generate-application-properties`, `generate-docker-resources`, `package-bundle-tomcat`, `package-bundle-tomcat-jarless`, `package-bundle-tomcat-sp`, `package-docker-image`.

## Key Version Properties

Defined in `parent/pom.xml`:
- `bonita.runtime.version` — Bonita Runtime BOM version (tracks `11.0-SNAPSHOT`)
- `branding.version` — User-facing version (`2026.1-SNAPSHOT`)
- `bonita-project-maven-plugin.version` — Bonita Maven Plugin version
- `maven-install-plugin.version` — Must follow `bonita-project-maven-plugin` version
- `maven-assembly-plugin.version` — Pinned to 3.6.0 pending upstream fix (#1236)

## Branch Strategy

- `dev` — Main development branch (default for PRs)
- `master`, `release-*` — Release branches
- `9.0.x`, `10.0.x`, `10.1.x`, etc. — Support branches
