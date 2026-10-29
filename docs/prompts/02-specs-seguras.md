# Prompt 02 — Enriquecer historias con requisitos de seguridad

Adjuntar: `docs/historia-beneficiarios.md`, `docs/arquitectura.md`, el threat model del Lab 1
y la carpeta `security/`.

```text
Tengo la historia funcional HU-101/HU-102 (adjunta), la arquitectura con sus trust
boundaries, el threat model STRIDE y las reglas internas en /security.

Usando OWASP ASVS 5.0 (capítulos V2 Validation and Business Logic, V4 API and Web Service,
V8 Authorization, V14 Data Protection, V16 Security Logging and Error Handling) y,
para la app móvil, MASVS (MASVS-AUTH, MASVS-STORAGE, MASVS-NETWORK, MASVS-PRIVACY):

1. Responde cada "pregunta que la historia no responde".
2. Genera criterios de seguridad SEC-xx verificables (uno por línea, medibles).
3. Para cada criterio indica: origen (amenaza STRIDE / capítulo ASVS-MASVS / regla interna).
4. Escribe escenarios Gherkin, incluyendo al menos 6 escenarios NEGATIVOS o de abuso.
5. Señala qué criterios requieren una decisión de arquitectura o de negocio (no de código).

No inventes números de requisito ASVS: cita el capítulo y describe el control.
```
