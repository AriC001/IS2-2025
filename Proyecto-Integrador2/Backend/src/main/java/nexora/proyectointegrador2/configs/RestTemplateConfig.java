package nexora.proyectointegrador2.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * Configuración independiente para beans reutilizables que no deben depender
 * del grafo de seguridad para evitar ciclos en el ApplicationContext.
 */
@Configuration
public class RestTemplateConfig {

  @Bean
  public RestTemplate restTemplate() {
    return new RestTemplate();
  }

}
