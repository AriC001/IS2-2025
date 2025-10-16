package com.practica.ej2consumer.business.persistence.rest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
import static org.springframework.test.web.client.ExpectedCount.once;
import org.springframework.test.web.client.MockRestServiceServer;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;
import org.springframework.web.client.RestTemplate;

import com.practica.ej2consumer.business.domain.dto.AutorDTO;

class AutorDAORestTest {

    @Test
    void findAllActivesShouldReturnDtoList() {
        RestTemplate restTemplate = new RestTemplate();
        AutorDAORest autorDAORest = new AutorDAORest(restTemplate);
        ReflectionTestUtils.setField(autorDAORest, "baseUrl", "http://localhost");
        MockRestServiceServer mockServer = MockRestServiceServer.createServer(restTemplate);

        mockServer.expect(once(), requestTo("http://localhost/autores"))
                  .andRespond(withSuccess("[{\"id\":1,\"nombre\":\"Ana\",\"apellido\":\"García\",\"biografia\":\"Autora destacada\"}]",
                                        MediaType.APPLICATION_JSON));

        List<AutorDTO> autores = autorDAORest.findAllActives();

        mockServer.verify();
        assertThat(autores).hasSize(1);
    AutorDTO autor = autores.get(0);
        assertThat(autor.getId()).isEqualTo(1L);
        assertThat(autor.getNombre()).isEqualTo("Ana");
        assertThat(autor.getApellido()).isEqualTo("García");
        assertThat(autor.getBiografia()).isEqualTo("Autora destacada");
    }
}
