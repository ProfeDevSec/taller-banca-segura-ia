package cl.develrox.taller.banca;

import static org.assertj.core.api.Assertions.assertThat;

import cl.develrox.taller.banca.beneficiary.domain.Beneficiary;
import cl.develrox.taller.banca.beneficiary.domain.BeneficiaryRepository;
import cl.develrox.taller.banca.beneficiary.domain.BeneficiaryStatus;
import cl.develrox.taller.banca.transfer.domain.TransferRepository;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.boot.resttestclient.TestRestTemplate;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureTestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

/**
 * Pruebas de aceptacion de seguridad derivadas del threat model (STRIDE), de ASVS y de las reglas /security.
 * En la version vulnerable del taller estas pruebas FALLAN. El objetivo del taller es dejarlas en verde.
 *
 * Ejecutar: ./gradlew test -Pseguridad
 */
@Tag("security")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestRestTemplate
@ExtendWith(OutputCaptureExtension.class)
class SecurityAcceptanceTests {

    static final UUID BEN_ANA_MAMA = UUID.fromString("3f6c1a52-8d4e-4b1a-9c2f-5a7e1d0b9001");
    static final UUID BEN_BRUNO_SOCIO = UUID.fromString("7b2e9d14-6a3c-4f8e-b1d5-2c9a8e4f9102");
    static final UUID BEN_BRUNO_PENDIENTE = UUID.fromString("e9a85f26-4c1d-4e7b-a3f9-6d2c5b8e9304");
    static final String CUENTA_BRUNO_SOCIO = "000087654321";
    static final String CUENTA_ANA_MAMA = "000012345678";

    @Autowired
    TestRestTemplate rest;
    @Autowired
    BeneficiaryRepository beneficiaries;
    @Autowired
    TransferRepository transfers;
    @Autowired
    ObjectMapper json;

    TestRestTemplate ana() {
        return rest.withBasicAuth("ana", "ana123");
    }

    TestRestTemplate bruno() {
        return rest.withBasicAuth("bruno", "bruno123");
    }

    static Map<String, Object> nuevoBeneficiario(String alias, String cuenta) {
        Map<String, Object> body = new HashMap<>();
        body.put("alias", alias);
        body.put("holderName", "Titular Prueba");
        body.put("holderRut", "77777777-7");
        body.put("bankCode", "016");
        body.put("accountNumber", cuenta);
        return body;
    }

    static Map<String, Object> transferencia(UUID beneficiario, Object monto) {
        Map<String, Object> body = new HashMap<>();
        body.put("beneficiaryId", beneficiario);
        body.put("amount", monto);
        body.put("currency", "CLP");
        return body;
    }

    static String alias() {
        return "T" + UUID.randomUUID().toString().substring(0, 8);
    }

    // ---------------------------------------------------------------- Spoofing

    @Test
    @DisplayName("SEC-01 | STRIDE-S: sin autenticacion no hay acceso a beneficiarios")
    void sec01_requiereAutenticacion() {
        ResponseEntity<String> r = rest.getForEntity("/api/beneficiaries", String.class);
        assertThat(r.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    // ------------------------------------------------- Elevation / IDOR (BOLA)

    @Test
    @DisplayName("SEC-02 | STRIDE-E / API1 BOLA: un cliente NO puede leer el beneficiario de otro cliente (UUID)")
    void sec02_noLeerBeneficiarioAjenoPorUuid() {
        ResponseEntity<String> r = ana().getForEntity("/api/beneficiaries/" + BEN_BRUNO_SOCIO, String.class);
        assertThat(r.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(r.getBody()).doesNotContain("Socio").doesNotContain(CUENTA_BRUNO_SOCIO);
    }

    @Test
    @DisplayName("SEC-02/SEC-03 | Enumeracion: GET /beneficiaries/91 no debe devolver datos de otro cliente")
    void sec03_enumeracionSecuencialNoExponeDatos() {
        ResponseEntity<String> r = ana().getForEntity("/api/beneficiaries/91", String.class);
        assertThat(r.getStatusCode()).isNotEqualTo(HttpStatus.OK);
        assertThat(r.getBody()).doesNotContain(CUENTA_BRUNO_SOCIO).doesNotContain("Diego Rojas");
    }

    @Test
    @DisplayName("SEC-03 | El identificador publico devuelto es un UUID, no el id interno")
    void sec03_idPublicoEsUuid() throws Exception {
        ResponseEntity<String> r = ana().postForEntity("/api/beneficiaries",
            nuevoBeneficiario(alias(), "000900800700"), String.class);
        assertThat(r.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        JsonNode body = json.readTree(r.getBody());
        assertThat(body.path("id").asText()).matches("[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}");
    }

    // ------------------------------------------------ Information disclosure

    @Test
    @DisplayName("SEC-04 | Las respuestas no exponen el numero de cuenta completo")
    void sec04_cuentaEnmascarada() {
        ResponseEntity<String> lista = ana().getForEntity("/api/beneficiaries", String.class);
        assertThat(lista.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(lista.getBody()).doesNotContain(CUENTA_ANA_MAMA).contains("5678");

        ResponseEntity<String> creado = ana().postForEntity("/api/beneficiaries",
            nuevoBeneficiario(alias(), "000444555666"), String.class);
        assertThat(creado.getBody()).doesNotContain("000444555666");
    }

    @Test
    @DisplayName("SEC-API-03 | La respuesta no expone campos internos de la entidad")
    void secApi03_sinCamposInternos() {
        ResponseEntity<String> r = ana().getForEntity("/api/beneficiaries", String.class);
        assertThat(r.getBody())
            .doesNotContain("internalRiskScore")
            .doesNotContain("customerId")
            .doesNotContain("createdBy");
    }

    // ------------------------------------------------------------- Tampering

    @Test
    @DisplayName("SEC-API-03 | CWE-915 Mass Assignment: no se puede fijar customerId, status ni riskScore")
    void secApi03_massAssignment() {
        String alias = alias();
        Map<String, Object> body = nuevoBeneficiario(alias, "000123123123");
        body.put("customerId", 2);
        body.put("status", "VERIFIED");
        body.put("internalRiskScore", 0);

        ana().postForEntity("/api/beneficiaries", body, String.class);

        Beneficiary guardado = beneficiaries.findFirstByAliasOrderByIdDesc(alias).orElseThrow();
        assertThat(guardado.getCustomerId()).isEqualTo(1L);
        assertThat(guardado.getStatus()).isEqualTo(BeneficiaryStatus.PENDING);
        assertThat((Object) guardado.getInternalRiskScore()).as("riskScore lo calcula el banco, no el cliente").isNotEqualTo(0);
    }

    @Test
    @DisplayName("SEC-07 | STRIDE-T: no se puede transferir a un beneficiario de otro cliente (UUID valido)")
    void sec07_transferenciaABeneficiarioAjeno() {
        long antes = transfers.count();
        ResponseEntity<String> r = ana().postForEntity("/api/transfers",
            transferencia(BEN_BRUNO_SOCIO, 10000), String.class);
        assertThat(r.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(transfers.count()).isEqualTo(antes);
    }

    @Test
    @DisplayName("SEC-07 | El customerId enviado por el cliente se ignora: la identidad viene del contexto autenticado")
    void sec07_customerIdDelRequestSeIgnora() {
        int transferenciasDeBruno = transfers.findByCustomerId(2L).size();
        Map<String, Object> body = transferencia(BEN_ANA_MAMA, 5000);
        body.put("customerId", 2);

        ana().postForEntity("/api/transfers", body, String.class);

        assertThat(transfers.findByCustomerId(2L)).hasSize(transferenciasDeBruno);
    }

    @ParameterizedTest(name = "monto = {0}")
    @NullSource
    @ValueSource(strings = {"-1000", "0", "600000", "abc"})
    @DisplayName("SEC-VAL-01 | Montos invalidos son rechazados con 400 y no generan transferencia")
    void secVal01_montosInvalidos(String monto) {
        long antes = transfers.count();
        ResponseEntity<String> r = ana().postForEntity("/api/transfers",
            transferencia(BEN_ANA_MAMA, monto), String.class);
        assertThat(r.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(transfers.count()).isEqualTo(antes);
    }

    @Test
    @DisplayName("SEC-08 | Regla de negocio: no se transfiere a beneficiarios PENDIENTES de verificacion")
    void sec08_beneficiarioPendiente() {
        long antes = transfers.count();
        ResponseEntity<String> r = bruno().postForEntity("/api/transfers",
            transferencia(BEN_BRUNO_PENDIENTE, 10000), String.class);
        assertThat(r.getStatusCode()).isEqualTo(HttpStatus.UNPROCESSABLE_CONTENT);
        assertThat(transfers.count()).isEqualTo(antes);
    }

    // ------------------------------------------------------ Errores y logging

    @Test
    @DisplayName("SEC-ERR-01 | Los errores no exponen stack traces, excepciones ni nombres de tablas")
    void secErr01_erroresSinDetallesInternos() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        ResponseEntity<String> malformado = ana().postForEntity("/api/transfers",
            new HttpEntity<>("{\"amount\": ", headers), String.class);
        ResponseEntity<String> inexistente = ana().getForEntity(
            "/api/beneficiaries/" + UUID.randomUUID(), String.class);

        for (ResponseEntity<String> r : java.util.List.of(malformado, inexistente)) {
            assertThat(r.getStatusCode().is5xxServerError()).isFalse();
            assertThat(r.getBody())
                .doesNotContain("trace")
                .doesNotContain("Exception")
                .doesNotContain("BENEFICIARY")
                .doesNotContain("at org.")
                .doesNotContain("at cl.");
        }
    }

    @Test
    @DisplayName("SEC-LOG-01 | Los logs no contienen credenciales ni numeros de cuenta completos")
    void secLog01_logsSinDatosSensibles(CapturedOutput output) {
        ana().postForEntity("/api/beneficiaries", nuevoBeneficiario(alias(), "000555666777"), String.class);
        ana().postForEntity("/api/transfers", transferencia(BEN_ANA_MAMA, 1500), String.class);

        assertThat(output.getAll())
            .doesNotContain("000555666777")
            .doesNotContain(CUENTA_ANA_MAMA)
            .doesNotContainIgnoringCase("authorization=Basic")
            .doesNotContain("YW5hOmFuYTEyMw"); // base64("ana:ana123")
    }

    @Test
    @DisplayName("SEC-ERR-01 | Rutas y métodos inexistentes responden 404/405 genéricos, nunca 500")
    void secErr01_rutasYMetodosInexistentes() {
        ResponseEntity<String> ruta = ana().getForEntity("/api/no-existe", String.class);
        ResponseEntity<String> metodo = ana().exchange("/api/transfers", HttpMethod.DELETE, null, String.class);

        assertThat(ruta.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(metodo.getStatusCode()).isEqualTo(HttpStatus.METHOD_NOT_ALLOWED);
        for (ResponseEntity<String> r : java.util.List.of(ruta, metodo)) {
            assertThat(r.getBody()).doesNotContain("trace").doesNotContain("Exception");
        }
    }

    @Test
    @DisplayName("SEC-05 | El alta de beneficiario y la transferencia generan eventos de auditoría")
    void sec05_eventosDeAuditoria(CapturedOutput output) {
        ana().postForEntity("/api/beneficiaries", nuevoBeneficiario(alias(), "000222333444"), String.class);
        ana().postForEntity("/api/transfers", transferencia(BEN_ANA_MAMA, 2500), String.class);

        assertThat(output.getAll())
            .contains("event=BENEFICIARY_CREATED")
            .contains("event=TRANSFER_EXECUTED");
    }

    @Test
    @DisplayName("SEC-LOG-02 | Cada respuesta incluye un correlationId para trazabilidad")
    void secLog02_correlationId() {
        ResponseEntity<String> r = ana().getForEntity("/api/beneficiaries", String.class);
        assertThat(r.getHeaders().getFirst("X-Correlation-Id")).isNotBlank();
    }
}
