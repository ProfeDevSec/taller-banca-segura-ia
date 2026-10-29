# Reglas de arquitectura

## SEC-ARCH-01 — Capas definidas
El flujo es **Controller → Application Service → Domain → Repository**.
- Los controladores no acceden a repositorios.
- La lógica de negocio y de autorización vive en el servicio de aplicación, no en el controlador.
- *Verificación:* `ArchitectureRulesTest.controladoresNoAccedenARepositorios`.

## SEC-ARCH-02 — Contratos explícitos entre capas
Cada boundary tiene su propio contrato:
- Entrada HTTP → `*Request` (DTO con validaciones).
- Salida HTTP → `*Response` (DTO mínimo).
- Persistencia → `@Entity` (nunca sale de `application`/`domain`).
- La conversión se hace en un `*Mapper` explícito y revisable.

## SEC-ARCH-03 — La IA opera dentro de la arquitectura
Un agente de IA **no decide** la arquitectura en cada archivo. Si una tarea requiere una capa,
patrón o dependencia nueva, el agente debe proponerla y detenerse para revisión humana.
No se agregan dependencias (librerías) sin aprobación: verificar que el paquete exista,
sea el oficial y no esté vulnerable (riesgo de *package hallucination / slopsquatting*).
