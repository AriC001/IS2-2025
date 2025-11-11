package com.nexora.proyecto.gestion.business.persistence.dao;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nexora.proyecto.gestion.dto.AlquilerDTO;

@Component
public class AlquilerDAO extends BaseDAO<AlquilerDTO, String> {

  @Autowired
  public AlquilerDAO(RestTemplate restTemplate) {
    super(restTemplate, "/alquileres");
  }

  @Override
  protected Class<AlquilerDTO> getEntityClass() {
    return AlquilerDTO.class;
  }

  /**
   * Guarda un alquiler con documento adjunto usando multipart/form-data.
   */
  public AlquilerDTO saveWithDocument(AlquilerDTO entity, MultipartFile archivoDocumento) {
    if (entity == null) {
      throw new IllegalArgumentException("La entidad no puede ser null");
    }
    try {
      String url = baseUrl + entityPath;
      logger.debug("Guardando alquiler con documento en: {}", url);

      // Convertir el DTO a JSON string
      ObjectMapper objectMapper = new ObjectMapper();
      String alquilerJson = objectMapper.writeValueAsString(entity);

      // Crear MultiValueMap para multipart/form-data
      MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
      body.add("alquiler", alquilerJson);
      
      if (archivoDocumento != null && !archivoDocumento.isEmpty()) {
        ByteArrayResource resource = new ByteArrayResource(archivoDocumento.getBytes()) {
          @Override
          public String getFilename() {
            return archivoDocumento.getOriginalFilename();
          }
        };
        body.add("archivoDocumento", resource);
      }

      // Configurar headers para multipart
      HttpHeaders headers = new HttpHeaders();
      headers.setContentType(MediaType.MULTIPART_FORM_DATA);

      HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

      // Enviar request
      ResponseEntity<AlquilerDTO> response = restTemplate.exchange(
          url,
          HttpMethod.POST,
          requestEntity,
          AlquilerDTO.class
      );

      logger.info("Alquiler con documento guardado exitosamente con ID: {}", 
          response.getBody() != null ? response.getBody().getId() : "N/A");
      return response.getBody();
    } catch (IOException e) {
      logger.error("Error al procesar archivo: {}", e.getMessage());
      throw new RuntimeException("Error al procesar el archivo: " + e.getMessage(), e);
    } catch (HttpClientErrorException e) {
      logger.error("Error del cliente al guardar alquiler con documento: {} - {}", e.getStatusCode(), e.getMessage());
      throw new RuntimeException("Error al guardar: " + e.getStatusCode() + " - " + e.getMessage(), e);
    } catch (HttpServerErrorException e) {
      logger.error("Error del servidor al guardar alquiler con documento: {}", e.getMessage());
      throw new RuntimeException("Error en el servidor: " + e.getStatusCode() + " - " + e.getMessage(), e);
    } catch (ResourceAccessException e) {
      logger.error("Error de conexión con la API al guardar alquiler con documento");
      throw new RuntimeException("No se pudo conectar con la API", e);
    } catch (RestClientException e) {
      logger.error("Error al guardar alquiler con documento: {}", e.getMessage());
      throw new RuntimeException("Error al guardar el recurso: " + e.getMessage(), e);
    }
  }

  /**
   * Actualiza un alquiler con documento adjunto usando multipart/form-data.
   */
  public AlquilerDTO updateWithDocument(String id, AlquilerDTO entity, MultipartFile archivoDocumento) {
    if (id == null) {
      throw new IllegalArgumentException("ID no puede ser null");
    }
    if (entity == null) {
      throw new IllegalArgumentException("La entidad no puede ser null");
    }
    try {
      String url = baseUrl + entityPath + "/" + id;
      logger.debug("Actualizando alquiler con documento con ID: {}", id);

      // Convertir el DTO a JSON string
      ObjectMapper objectMapper = new ObjectMapper();
      String alquilerJson = objectMapper.writeValueAsString(entity);

      // Crear MultiValueMap para multipart/form-data
      MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
      body.add("alquiler", alquilerJson);
      
      if (archivoDocumento != null && !archivoDocumento.isEmpty()) {
        ByteArrayResource resource = new ByteArrayResource(archivoDocumento.getBytes()) {
          @Override
          public String getFilename() {
            return archivoDocumento.getOriginalFilename();
          }
        };
        body.add("archivoDocumento", resource);
      }

      // Configurar headers para multipart
      HttpHeaders headers = new HttpHeaders();
      headers.setContentType(MediaType.MULTIPART_FORM_DATA);

      HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

      // Enviar request
      ResponseEntity<AlquilerDTO> response = restTemplate.exchange(
          url,
          HttpMethod.PUT,
          requestEntity,
          AlquilerDTO.class
      );

      logger.info("Alquiler con documento actualizado exitosamente con ID: {}", id);
      return response.getBody();
    } catch (IOException e) {
      logger.error("Error al procesar archivo: {}", e.getMessage());
      throw new RuntimeException("Error al procesar el archivo: " + e.getMessage(), e);
    } catch (HttpClientErrorException.NotFound e) {
      logger.warn("No se encontró alquiler con ID: {} para actualizar", id);
      throw new com.nexora.proyecto.gestion.exception.EntityNotFoundException("No se encontró el recurso con ID: " + id);
    } catch (HttpClientErrorException e) {
      logger.error("Error del cliente al actualizar alquiler con documento: {} - {}", e.getStatusCode(), e.getMessage());
      throw new RuntimeException("Error al actualizar: " + e.getStatusCode() + " - " + e.getMessage(), e);
    } catch (HttpServerErrorException e) {
      logger.error("Error del servidor al actualizar alquiler con documento: {}", e.getMessage());
      throw new RuntimeException("Error en el servidor: " + e.getStatusCode() + " - " + e.getMessage(), e);
    } catch (ResourceAccessException e) {
      logger.error("Error de conexión con la API al actualizar alquiler con documento");
      throw new RuntimeException("No se pudo conectar con la API", e);
    } catch (RestClientException e) {
      logger.error("Error al actualizar alquiler con documento: {}", e.getMessage());
      throw new RuntimeException("Error al actualizar el recurso: " + e.getMessage(), e);
    }
  }

}

