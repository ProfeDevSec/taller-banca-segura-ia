# Taller — Desarrollo Seguro en la Era de la IA

**Develrox** · Caso ficticio: *"Permitir que un cliente registre beneficiarios y realice transferencias a cuentas previamente registradas."*

> ⚠️ Esta aplicación es **intencionalmente vulnerable**. Ejecútela solo en su equipo (localhost).
> Todos los datos, RUT, cuentas, tokens y llaves del repositorio son **ficticios**.

## Requisitos
- JDK 21 o superior (Spring Boot 4.1)
- Gradle 9 (o el wrapper `./gradlew`, incluido: no requiere instalar Gradle)
- Un IDE (IntelliJ IDEA / VS Code) y **su** asistente de IA (Copilot, Claude Code, Cursor u otro)
- `curl` o el cliente HTTP del IDE
- Opcional: [Semgrep](https://semgrep.dev) (`pip install semgrep`) para las reglas estáticas

### Alternativa sin instalar Java: Dev Container
Si su equipo corporativo no tiene JDK 21, abra el proyecto con **Dev Containers** (VS Code o IntelliJ):
la configuración está en `.devcontainer/` y descarga las dependencias al crear el contenedor.

## Levantar la aplicación
Linux, macOS o Git Bash:
```bash
./gradlew bootRun
```
Windows (PowerShell o cmd):
```bat
.\gradlew.bat bootRun
```
API en `http://localhost:8080`. Usuarios de prueba (HTTP Basic):

| Usuario | Clave | Cliente | Beneficiarios semilla |
|---|---|---|---|
| ana | ana123 | 1 | 90 *Mama* (VERIFIED), 92 *Arriendo* (VERIFIED) |
| bruno | bruno123 | 2 | 91 *Socio* (VERIFIED), 93 *Hermano* (PENDING) |

## Endpoints
| Método | Ruta | Descripción |
|---|---|---|
| GET | `/api/beneficiaries` | Lista mis beneficiarios |
| GET | `/api/beneficiaries/{id}` | Detalle de un beneficiario |
| POST | `/api/beneficiaries` | Alta de beneficiario |
| POST | `/api/transfers` | Transferencia a un beneficiario |

> La aplicación es solo una API: no tiene página en `/`. Si el navegador muestra una página de error con
> detalle técnico, la aplicación está funcionando; analizar esas respuestas es parte del Lab 3.

## Pruebas y controles automáticos
| Comando | Qué verifica | Estado inicial |
|---|---|---|
| `./gradlew test` | happy path | verde |
| `./gradlew test -Pseguridad` | 24 pruebas: seguridad + ArchUnit | 19 fallas |
| `semgrep scan --config security/semgrep` | reglas estáticas | con hallazgos |

En Windows use `.\gradlew.bat` en lugar de `./gradlew`.

**Objetivo del taller:** llegar a `Tests run: 24, Failures: 0` con `-Pseguridad` y a 0 hallazgos de Semgrep,
usando IA **dentro** de las reglas de `/security`, sin modificar las pruebas.
El detalle de cada prueba queda en `build/reports/tests/test/index.html`.

## Estructura
```
security/              Reglas de desarrollo seguro (contexto para humanos y agentes)
security/semgrep/      Reglas estáticas que verifican parte de esas reglas
docs/                  Arquitectura, historia de usuario, prompts por laboratorio, desafío opcional
http/                  requests.http, ataques.sh, ataques.ps1
src/test/...           SecurityAcceptanceTests (STRIDE/ASVS) + ArchitectureRulesTest (ArchUnit)
.devcontainer/         Entorno reproducible (JDK 21)
.env, secrets/, dumps/ ANTIPATRONES ficticios para el laboratorio de contexto del agente
```

## Laboratorios
Los prompts de cada laboratorio están en `docs/prompts/`. La facilitación (puntos de control y respuestas
esperadas) está en la Guía del Relator, sección 8.

| Lab | Tema | Tiempo | Material |
|---|---|---|---|
| 0 | Preparación y reconocimiento | 15 min | este README, `security/` |
| 1 | Threat modeling STRIDE asistido por IA | 25 min | `docs/arquitectura.md`, `docs/prompts/01-*` |
| 2 | De historia funcional a spec segura (ASVS/MASVS) | 20 min | `docs/historia-beneficiarios.md`, `docs/prompts/02-*` |
| 3 | Pensar como atacante: explotar el caso | 20 min | `http/` |
| 4 | Contexto del agente: reglas, exclusiones y secretos | 20 min | `security/secrets.md` |
| 5 | Generación y revisión de código con IA | 30 min | `docs/prompts/03-*`, `docs/prompts/05-*` |
| 6 | Pruebas de seguridad y Security as Code | 20 min | `docs/prompts/04-*`, `security/semgrep/` |
| 7 | De la vulnerabilidad al riesgo (CVSS) | 10 min | hallazgos del Lab 3 |
| + | Desafío opcional: abuso por automatización | — | `docs/desafio-rate-limiting.md` |
