# Prompt 04 — Pruebas de seguridad derivadas del threat model

```text
A partir de la matriz de trazabilidad (amenaza STRIDE -> requisito SEC -> regla), genera
pruebas JUnit 5 + Spring Boot Test (TestRestTemplate, @SpringBootTest RANDOM_PORT) que
verifiquen comportamiento, no implementación:

- Autorización horizontal: ana intenta leer/modificar/transferir sobre recursos de bruno.
- Manipulación de parámetros: customerId, status, internalRiskScore en el body.
- Valores límite de amount: 0, negativo, 500000, 500000.01, null, texto, notación científica.
- Respuestas y errores sin datos sensibles (cuenta completa, stack trace, nombres de tabla).
- Logs sin secretos (usar OutputCaptureExtension).

Cada prueba debe tener @DisplayName con el ID del requisito (ej. "SEC-02 | ...").
Usa usuarios de prueba ana/ana123 y bruno/bruno123 y los UUID semilla de data.sql.
No uses datos reales.
```

## Revisión humana de las pruebas generadas
- [ ] ¿La prueba falla contra la versión vulnerable? (si pasa en ambas, no prueba nada)
- [ ] ¿Verifica el **estado** (no se creó la transferencia) y no solo el código HTTP?
- [ ] ¿Usa aserciones débiles (`isNotNull`, `is2xx`) donde debería ser específica?
