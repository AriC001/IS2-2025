package com.nexora.proyecto.gestion.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/imagenes")
public class ImagenController {

  private static final Logger logger = LoggerFactory.getLogger(ImagenController.class);

  @Autowired
  private RestTemplate restTemplate;

  @Value("${api.base.url}")
  private String baseUrl;

  /**
   * Proxy para obtener la imagen desde el backend y devolverla como respuesta HTTP.
   * Esto permite mostrar las imágenes en las etiquetas <img> sin problemas de CORS.
   */
  @GetMapping("/{id}")
  public ResponseEntity<byte[]> obtenerImagen(@PathVariable String id, HttpSession session) {
    // Verificar sesión
    if (session.getAttribute("token") == null) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    try {
      String url = baseUrl + "/imagenes/" + id + "/imagen";
      logger.debug("Obteniendo imagen desde: {}", url);
      
      ResponseEntity<byte[]> response = restTemplate.getForEntity(url, byte[].class);
      
      byte[] body = response.getBody();
      if (body == null || body.length == 0) {
        logger.warn("La imagen con ID {} está vacía o no existe", id);
        return ResponseEntity.notFound().build();
      }

      HttpHeaders headers = new HttpHeaders();
      MediaType contentType = response.getHeaders().getContentType();
      if (contentType != null) {
        headers.setContentType(contentType);
      } else {
        headers.setContentType(MediaType.IMAGE_JPEG);
      }
      headers.setContentLength(body.length);
      headers.setCacheControl("max-age=3600");

      logger.debug("Imagen obtenida exitosamente. Tamaño: {} bytes, Tipo: {}", 
          body.length, contentType);

      return new ResponseEntity<>(body, headers, HttpStatus.OK);
      
    } catch (HttpClientErrorException.NotFound e) {
      logger.warn("Imagen con ID {} no encontrada", id);
      return ResponseEntity.notFound().build();
    } catch (Exception e) {
      logger.error("Error al obtener imagen con ID {}: {}", id, e.getMessage(), e);
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
  }
}

