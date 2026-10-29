package cl.develrox.taller.banca;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.resttestclient.TestRestTemplate;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureTestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestRestTemplate
class HappyPathTest {

    @Autowired
    TestRestTemplate rest;

    @Test
    @DisplayName("Happy path: un cliente autenticado lista sus beneficiarios")
    void clienteListaSusBeneficiarios() {
        ResponseEntity<String> response = rest.withBasicAuth("ana", "ana123")
            .getForEntity("/api/beneficiaries", String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).contains("Mama").contains("Arriendo").doesNotContain("Socio");
    }
}
