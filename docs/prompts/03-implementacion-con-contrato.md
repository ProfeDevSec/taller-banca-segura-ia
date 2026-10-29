# Prompt 03 — Implementación con contrato técnico y de seguridad

## A. Prompt pobre (para comparar)
```text
Implementa un endpoint para crear beneficiarios y otro para hacer transferencias.
```

## B. Prompt con contexto de ingeniería y seguridad
```text
Refactoriza el caso de uso CreateBeneficiary / GetBeneficiary / CreateTransfer de este repo
respetando las reglas de /security (léelas antes de empezar) y:

1. Arquitectura definida: Controller -> Application Service -> Domain -> Repository.
2. No exponer entidades de persistencia (SEC-API-03).
3. DTOs de entrada/salida explícitos con validaciones (SEC-VAL-01). Records de Java.
4. IDs externos UUID (publicId); no exponer IDs internos (SEC-ID-01).
5. La identidad del cliente proviene del contexto autenticado, no del request (SEC-AUTH-02).
6. Verificar ownership en toda lectura/modificación, en la misma consulta (SEC-AUTHZ-02).
7. No registrar números de cuenta completos, headers ni secretos (SEC-LOG-01).
8. Usar BeneficiaryMapper / TransferMapper para cruzar Entity <-> DTO.
9. Manejo de errores corporativo: ProblemDetail genérico + correlationId (SEC-ERR-01).
10. No transferir a beneficiarios PENDING (SEC-AUTH-03).

Restricciones:
- No agregues dependencias nuevas al build.gradle.
- No modifiques las pruebas existentes en src/test.
- Al terminar, ejecuta: ./gradlew test -Pseguridad  y reporta el resultado.
- Si una regla entra en conflicto con otra, detente y pregunta.
```
