# Reglas de secretos y contexto del agente

## SEC-SECRETS-01 — Sin secretos en el repositorio
Configuración solo con **referencias**: `${DB_PASSWORD}`, `${CORE_API_TOKEN}`, `${PRIVATE_KEY_PATH}`.
Los valores se resuelven en runtime desde Secret Manager / Vault / variables del entorno de ejecución.

## SEC-SECRETS-02 — Nada sensible en el contexto de la IA
Nunca entregar al agente: `.env`, configuraciones productivas, certificados, llaves, dumps,
datos de clientes ni tokens. **Si un secreto entra al contexto, ya amplió su superficie de
exposición** (prompt, historial, logs de la herramienta, proveedor), aunque el agente no tenga
mala intención. Un secreto expuesto se **rota**, no se "borra".

## SEC-SECRETS-03 — Exclusión explícita
Configurar las exclusiones de cada herramienta:

| Herramienta | Mecanismo |
|---|---|
| Claude Code | `.claude/settings.json` → `permissions.deny` (`Read(./.env)`, `Read(./secrets/**)`) |
| Cursor | `.cursorignore` |
| JetBrains AI Assistant | `.aiignore` (activarlo en *Settings → Tools → AI Assistant → Project Settings*); `.noai` desactiva la IA en todo el proyecto |
| GitHub Copilot | *Content exclusion* en la configuración del repositorio/organización (solo planes Business/Enterprise). Según la documentación de GitHub no cubre todos los modos (p. ej. modo agente o CLI): verificar la tabla vigente |
| Todas | `.gitignore` + escáner de secretos en pre-commit y CI |

> Las exclusiones reducen la exposición, pero **no son una garantía**: el control de fondo es que los secretos
> no existan en el repositorio ni en el entorno de desarrollo.

## SEC-SECRETS-04 — Mínimo privilegio para el agente
El agente recibe solo los permisos que la tarea necesita: lectura acotada, escritura en `src/`
y `docs/`, ejecución de `./gradlew test`, **sin** acceso de red libre, sin `git push`, sin acceso a
credenciales ni a entornos productivos. Confirmación humana para acciones irreversibles.
