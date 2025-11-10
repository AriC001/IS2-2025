package com.practica.ej2consumer.controller.view;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import com.practica.ej2consumer.business.domain.dto.AutorDTO;
import com.practica.ej2consumer.business.logic.service.AutorService;

@WebMvcTest(controllers = AutorController.class)
class AutorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AutorService autorService;

    @Test
    void listarAutoresDebeCargarVistaConDatos() throws Exception {
    AutorDTO autor = AutorDTO.builder()
        .nombre("Ana")
        .apellido("García")
        .biografia("Autora destacada")
        .build();
    autor.setId(1L);

        when(autorService.findAllActives()).thenReturn(List.of(autor));

        mockMvc.perform(get("/autores"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("entities", "entityName"))
                .andExpect(model().attribute("entities", hasSize(1)))
                .andExpect(view().name("autores/lista"));
    }

        @TestConfiguration
        static class AutorControllerTestConfig {

            @Bean
            AutorService autorService() {
                return org.mockito.Mockito.mock(AutorService.class);
            }
        }
}
