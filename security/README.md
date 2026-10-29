# /security — Reglas de desarrollo seguro (humanos + agentes de IA)

Estas reglas son **contexto versionado**: las leen desarrolladores, revisores y agentes de IA.
Cada regla tiene un identificador estable para poder citarla en historias, prompts, PRs y pruebas.

| Archivo | Ámbito | Reglas |
|---|---|---|
| [architecture-rules.md](architecture-rules.md) | Capas, contratos, dependencias | SEC-ARCH-01..03 |
| [api-security.md](api-security.md) | IDs, DTOs, mappers, validación, autorización | SEC-ID-01, SEC-API-01..03, SEC-VAL-01, SEC-AUTHZ-01..02 |
| [authentication.md](authentication.md) | Identidad y operaciones sensibles | SEC-AUTH-01..03 |
| [data-handling.md](data-handling.md) | Minimización, enmascaramiento, datos personales | SEC-DATA-01..04 |
| [secrets.md](secrets.md) | Secretos y contexto del agente | SEC-SECRETS-01..04 |
| [logging.md](logging.md) | Errores, logs y auditoría | SEC-ERR-01, SEC-LOG-01..03 |

## Instrucción ≠ garantía: de la regla al control verificable

Un agente (o una persona) puede ignorar una regla escrita. Por eso cada regla importante
debe tener **al menos un control automático** que la haga cumplir:

| Regla | Control automático en este repo |
|---|---|
| SEC-API-03 Entidades fuera de los controladores | `ArchitectureRulesTest` (ArchUnit) |
| SEC-ARCH-01 Controller → Service → Repository | `ArchitectureRulesTest` (ArchUnit) |
| SEC-AUTHZ-02 Ownership en cada acceso | `SecurityAcceptanceTests.sec02_*`, `sec07_*` · Semgrep `sec-authz-02-*` |
| SEC-ID-01 / SEC-03 IDs externos no predecibles | `SecurityAcceptanceTests.sec03_*` · Semgrep `sec-id-01-*` |
| SEC-VAL-01 Contratos de entrada | `SecurityAcceptanceTests.secVal01_*` |
| SEC-05 Auditoría de operaciones | `SecurityAcceptanceTests.sec05_*` |
| SEC-ERR-01 Errores genéricos (incluye 404/405) | `SecurityAcceptanceTests.secErr01_*` · Semgrep `sec-err-01-*` |
| SEC-LOG-01 Sin datos sensibles en logs | `SecurityAcceptanceTests.secLog01_*` · Semgrep `sec-log-01-*` |
| SEC-SECRETS-01 Sin secretos en el repo | gitleaks (pre-commit y CI) · Semgrep `sec-secrets-01-*` |

Ejecutar las verificaciones:

- 24 pruebas funcionales, de seguridad y ArchUnit:
  ```bash
  ./gradlew test -Pseguridad
  ```
- Reglas estáticas del banco (`security/semgrep`):
  ```bash
  semgrep scan --config security/semgrep
  ```

Las reglas de Semgrep son **patrones de apoyo**, no una prueba de seguridad completa: detectan los
antipatrones más comunes del caso; las pruebas de comportamiento siguen siendo la evidencia principal.
