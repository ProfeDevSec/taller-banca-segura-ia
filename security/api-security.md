# Reglas de seguridad de APIs

## SEC-ID-01 — Identificadores internos vs. externos
Los identificadores internos de persistencia (`Long id`, secuencias) **nunca** se exponen como
identificadores públicos. Usar un identificador externo no predecible (UUID/ULID): `publicId`.
> Un UUID reduce la enumeración y el acoplamiento con la persistencia, **pero no reemplaza la autorización**.

## SEC-API-01 — Respuestas mínimas
La respuesta devuelve solo lo que el caso de uso necesita. Nada de `customerId`,
`internalRiskScore`, `createdBy`, `statusFlags` ni campos técnicos.

## SEC-API-02 — Rechazar lo que no se espera
Los campos que el cliente no debe controlar (dueño, estado, puntajes, roles) **no existen** en el DTO de entrada.

## SEC-API-03 — Entidades fuera del contrato
Las entidades de persistencia nunca se usan como contrato de entrada o salida de una API.
Evita *Mass Assignment* (CWE-915) y fugas de campos agregados a futuro.
- *Verificación:* `ArchitectureRulesTest` (ArchUnit).

## SEC-VAL-01 — Validación en el límite de confianza
Todo dato que cruza un límite de confianza se valida con un contrato explícito
(Bean Validation: `@NotNull`, `@Positive`, `@DecimalMax`, `@Pattern`, `@Size`) **antes** de llegar
a la lógica de dominio o persistencia. Validar tipo, formato, rango y longitud.

## SEC-AUTHZ-01 — Autorización en el backend
La autorización se decide en el backend en cada operación. Nunca confiar en ocultar botones,
en el orden de pantallas ni en valores enviados por el cliente.

## SEC-AUTHZ-02 — Autorización por relación con el recurso (anti-IDOR / BOLA)
Toda operación sobre un recurso de cliente valida la relación entre la identidad autenticada
y el recurso solicitado — idealmente **en la misma consulta**:

```java
// MAL: solo por id
beneficiaryRepository.findByPublicId(publicId);
// BIEN: id + dueño
beneficiaryRepository.findByPublicIdAndCustomerId(publicId, currentCustomer.id());
```

- Nunca usar `customerId` recibido desde el cliente como fuente de autorización.
- Responder **404** tanto si no existe como si no pertenece (no confirmar existencia).
