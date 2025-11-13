package com.nexora.proyecto.gestion.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.nexora.proyecto.gestion.config.interceptor.CambioClaveInterceptor;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

  private final CambioClaveInterceptor cambioClaveInterceptor;

  public WebMvcConfig(CambioClaveInterceptor cambioClaveInterceptor) {
    this.cambioClaveInterceptor = cambioClaveInterceptor;
  }

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(cambioClaveInterceptor)
        .addPathPatterns("/**")
        .excludePathPatterns(
            "/auth/**",
            "/css/**",
            "/js/**",
            "/vendor/**",
            "/img/**",
            "/imagenes/**",
            "/error"
        );
  }

}

