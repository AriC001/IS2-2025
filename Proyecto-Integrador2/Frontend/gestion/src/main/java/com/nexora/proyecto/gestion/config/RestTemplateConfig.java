package com.nexora.proyecto.gestion.config;

import java.util.Collections;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {
  
  @Bean
  public RestTemplate restTemplate() {
    RestTemplate restTemplate = new RestTemplate();
    
    // Agregar el interceptor para incluir el token JWT en todas las peticiones
    restTemplate.setInterceptors(Collections.singletonList(new JwtRequestInterceptor()));
    
    return restTemplate;
  }
}