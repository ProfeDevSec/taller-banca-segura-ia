# Reglas de manejo de datos

## SEC-DATA-01 — Minimización
Recolectar, procesar, devolver y registrar solo los datos necesarios para la finalidad
(principio de la Ley 21.719 de Protección de Datos Personales y de ASVS V14 Data Protection).

## SEC-DATA-02 — Enmascaramiento
Los números de cuenta se devuelven y registran enmascarados: `****1234`.
Usar el componente único `Masking`, no implementaciones ad-hoc.

## SEC-DATA-03 — Datos de clientes fuera de entornos no productivos y de la IA
Prohibido usar datos reales de clientes en desarrollo, pruebas, prompts o contexto de agentes.
Usar datos sintéticos (ver `data.sql`).

## SEC-DATA-04 — Salidas autorizadas
Toda salida de datos hacia clientes o subsistemas debe estar autorizada, registrada u ofuscada
según su clasificación.
