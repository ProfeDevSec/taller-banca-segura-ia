# Reglas de errores, logging y auditoría

## SEC-ERR-01 — Errores genéricos hacia el cliente
No devolver stack traces, nombres de clases, tablas, consultas SQL ni detalles de infraestructura.
Usar Problem Details (RFC 9457) con un `correlationId` para soporte.
`spring.web.error.include-stacktrace=never`, `include-message=never` (Spring Boot 4; en Boot 3 el prefijo era `server.error`).

## SEC-LOG-01 — Nunca registrar
Secretos, tokens, headers `Authorization`, contraseñas, CVV, credenciales, números de cuenta
completos ni bodies completos de requests/responses. Cuidado con `toString()` de entidades.

## SEC-LOG-02 — Trazabilidad
Cada request lleva `correlationId` (MDC + header `X-Correlation-Id`). Un valor entrante solo se
acepta si cumple un formato seguro (evitar *log injection*).

## SEC-LOG-03 — Eventos de seguridad
Registrar en el logger `AUDIT`: alta de beneficiario, transferencia ejecutada/rechazada, fallas
de autorización. Formato `clave=valor`, con identificadores públicos y datos enmascarados.
El log de auditoría no debe convertirse en una copia de los datos sensibles.
