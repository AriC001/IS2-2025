package com.nexora.proyecto.gestion.config.security;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpSession;

/**
 * Interceptor que agrega el token JWT a todas las peticiones HTTP realizadas por RestTemplate.
 * Obtiene el token de la sesión HTTP actual.
 */
public class JwtRequestInterceptor implements ClientHttpRequestInterceptor {

  private static final Logger logger = LoggerFactory.getLogger(JwtRequestInterceptor.class);

  @Override
  public ClientHttpResponse intercept(
      HttpRequest request,
      byte[] body,
      ClientHttpRequestExecution execution) throws IOException {

    try {
      // Obtener la sesión HTTP actual
      ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
      
      if (attributes != null) {
        HttpSession session = attributes.getRequest().getSession(false);
        
        if (session != null) {
          String token = (String) session.getAttribute("token");
          
          if (token != null && !token.isEmpty()) {
            // Agregar el header Authorization con el formato Bearer <token>
            request.getHeaders().add("Authorization", "Bearer " + token);
            logger.debug("Token JWT agregado a la petición: {}", request.getURI());
          } else {
            logger.debug("No se encontró token en la sesión para la petición: {}", request.getURI());
          }
        } else {
          logger.debug("No hay sesión disponible para la petición: {}", request.getURI());
        }
      } else {
        logger.debug("No hay contexto de request disponible para la petición: {}", request.getURI());
      }
    } catch (Exception e) {
      logger.warn("Error al agregar token JWT a la petición: {}", e.getMessage());
      // Continuar con la petición aunque falle la adición del token
    }

    // Ejecutar la petición
    return execution.execute(request, body);
  }
}

