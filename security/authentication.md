# Reglas de autenticación e identidad

## SEC-AUTH-01 — Toda operación requiere identidad
Ningún endpoint de negocio es anónimo. La identidad proviene del token/sesión validado por la
infraestructura de seguridad (`SecurityContext`), no de parámetros del request.

## SEC-AUTH-02 — Identidad del cliente desde el contexto
Obtener el cliente con un componente único (`CurrentCustomer`). Prohibido leer `customerId`,
`rut` o `username` desde body, query o headers para decidir sobre qué cliente se opera.

## SEC-AUTH-03 — Operaciones sensibles
El alta de beneficiarios y las transferencias sobre cierto monto requieren un **segundo factor
o reautenticación** (definido por arquitectura). Un beneficiario nuevo queda `PENDING`
hasta completar esa verificación; no se puede transferir a un beneficiario `PENDING`.
