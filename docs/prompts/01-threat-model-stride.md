# Prompt 01 — Threat model STRIDE asistido

Adjuntar como contexto: `docs/arquitectura.md`.

```text
Actúa como arquitecto de seguridad de un banco. Analiza el flujo descrito en
docs/arquitectura.md utilizando STRIDE.

Para cada interacción entre componentes identifica:
- activo
- identidad involucrada
- trust boundary que se cruza
- categoría STRIDE y amenaza
- escenario de abuso concreto (cómo lo haría un atacante)
- impacto (técnico y de negocio)
- mitigación propuesta (indicando si es control de arquitectura, de código o de proceso)

Reglas:
- No asumas controles que no estén indicados en el diseño.
- Si te falta información para evaluar una amenaza, decláralo como PREGUNTA ABIERTA.
- Prioriza las 8 amenazas más relevantes para este caso; no generes una lista genérica.
- Entrega el resultado como tabla Markdown.
```

## Validación humana (obligatoria)
- [ ] ¿Las amenazas son específicas del caso o genéricas ("usar HTTPS", "parchear")?
- [ ] ¿Detectó la autorización por relación con el recurso (IDOR/BOLA) en beneficiarios y transferencias?
- [ ] ¿Detectó el `customerId` enviado por el cliente?
- [ ] ¿Consideró al agente de IA y al pipeline como parte de la superficie?
- [ ] ¿Inventó controles que el diseño no tiene?
- [ ] ¿Qué amenaza relevante NO apareció? (lógica de negocio, límites, beneficiario recién creado)
