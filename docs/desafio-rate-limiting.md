# Desafío opcional — Abuso por automatización (T-10 · STRIDE-D)

> Para quienes terminen antes. No forma parte de las 24 pruebas del taller.

## Contexto
El threat model identifica la amenaza **T-10**: altas o consultas masivas automatizadas de beneficiarios
(enumeración, degradación del servicio). La arquitectura asume que el API Gateway hace *rate limiting*
global, pero **no existe una cuota por cliente**: es una **pregunta abierta** de arquitectura y negocio.

## Decisiones que NO le corresponden al desarrollador ni a la IA
- ¿Cuántas altas de beneficiarios por cliente y por día son razonables?
- ¿El límite vive en el Gateway (por cliente/token) o en el servicio?
- ¿Qué pasa al superarlo? (`429 Too Many Requests`, bloqueo temporal, alerta a fraude)

## Propuesta de requisito (borrador para validar con negocio)
```
SEC-10  Un cliente no puede registrar más de N beneficiarios por día.
        Al superar el límite: 429 + Retry-After, evento AUDIT "BENEFICIARY_QUOTA_EXCEEDED".
        Origen: STRIDE-D (T-10) · ASVS V2 Validation and Business Logic (límites anti-automatización).
```

## Pasos sugeridos
1. Pida a la IA **opciones** (Gateway, filtro con contador por cliente, librería de rate limiting) con pros y
   contras, **sin implementar**. Elija una y justifíquela.
2. Escriba primero la prueba: N altas → 201; la N+1 → 429 y evento de auditoría.
3. Implemente respetando `/security` (sin dependencias nuevas sin aprobación: SEC-ARCH-03).
4. Agregue la regla a `security/api-security.md` y el control a la tabla de `security/README.md`.

## Para discutir
- ¿Qué impide que un atacante use muchos clientes distintos? (cuotas por dispositivo, por IP, detección de fraude)
- ¿Por qué un límite en memoria no sirve con varias instancias del servicio?
