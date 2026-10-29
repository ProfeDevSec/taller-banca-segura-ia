# Checklist — Revisión de código generado por IA

"Compila" y "los tests pasan" no significa "es seguro". Revisar cada PR asistido con:

## Contrato y datos
- [ ] ¿Algún controlador recibe o devuelve una `@Entity`?
- [ ] ¿El DTO de entrada contiene campos que el cliente no debe controlar (dueño, estado, rol, score)?
- [ ] ¿Las respuestas exponen IDs internos, cuentas completas o campos técnicos?

## Autorización
- [ ] ¿Cada consulta a un recurso de cliente filtra por el cliente autenticado?
- [ ] ¿Se lee algún identificador de cliente desde el request?
- [ ] ¿Se agregó algún `permitAll()`, se desactivó un filtro o se relajó CORS "para que funcione"?

## Errores y logs
- [ ] ¿Hay `catch (Exception e)` que devuelve `e.getMessage()` al cliente?
- [ ] ¿Se registra un DTO/entidad completo, un header o un body?

## Dependencias y configuración
- [ ] ¿Se agregaron librerías? ¿Existen, son oficiales, están mantenidas, sin CVE conocidos?
- [ ] ¿Se agregaron secretos, URLs productivas o flags de depuración?

## Pruebas
- [ ] ¿El agente modificó o borró pruebas para que pasen?
- [ ] ¿Hay pruebas negativas para cada regla tocada?

## Trazabilidad
- [ ] ¿El PR indica qué partes generó la IA, con qué prompt/reglas y quién las revisó?
