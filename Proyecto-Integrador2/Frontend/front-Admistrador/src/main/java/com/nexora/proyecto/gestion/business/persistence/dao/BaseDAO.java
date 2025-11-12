package com.nexora.proyecto.gestion.business.persistence.dao;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.nexora.proyecto.gestion.dto.BaseDTO;
import com.nexora.proyecto.gestion.exception.EntityNotFoundException;


@Component
public abstract class BaseDAO<T extends BaseDTO, ID> {
    
  protected final Logger logger = LoggerFactory.getLogger(this.getClass());
    
  @Autowired
  protected RestTemplate restTemplate;

  @Value("${api.base.url}")
  protected String baseUrl;

  protected String entityPath;

  public BaseDAO(RestTemplate restTemplate,
                    String entityPath) {
    this.restTemplate = restTemplate;
    this.entityPath = entityPath;
  }

  public List<T> findAllActives() {
    try {
      String url = baseUrl + entityPath;
      logger.debug("Obteniendo todas las entidades activas desde: {}", url);
      ResponseEntity<T[]> response = restTemplate.getForEntity(url, getArrayClass());
      T[] body = response.getBody();
      List<T> result = body != null ? Arrays.asList(body) : List.of();
      logger.info("Se obtuvieron {} entidades activas desde {}", result.size(), url);
      if (result.isEmpty()) {
        logger.warn("No se obtuvieron entidades activas desde {}", url);
      }
      return result;
    } catch (HttpClientErrorException e) {
      logger.error("Error del cliente (HTTP {}) al obtener entidades desde {}: {}", 
          e.getStatusCode(), baseUrl + entityPath, e.getMessage());
      logger.error("Response body: {}", e.getResponseBodyAsString());
      // Si es un error 401 o 403, podría ser un problema de autenticación
      if (e.getStatusCode().value() == 401 || e.getStatusCode().value() == 403) {
        logger.error("Error de autenticación/autorización. Verificar que el token JWT esté en la sesión.");
      }
      throw new RuntimeException("Error del cliente al obtener entidades: " + e.getStatusCode() + " - " + e.getMessage(), e);
    } catch (HttpServerErrorException e) {
      logger.error("Error del servidor (HTTP {}) al obtener entidades desde {}: {}", 
          e.getStatusCode(), baseUrl + entityPath, e.getMessage());
      logger.error("Response body: {}", e.getResponseBodyAsString());
      throw new RuntimeException("Error en el servidor: " + e.getStatusCode() + " - " + e.getMessage(), e);
    } catch (ResourceAccessException e) {
      logger.error("Error de conexión con la API: {}", baseUrl + entityPath, e);
      throw new RuntimeException("No se pudo conectar con la API: " + baseUrl + entityPath, e);
    } catch (RestClientException e) {
      logger.error("Error al conectar con la API: {}", e.getMessage(), e);
      throw new RuntimeException("Error conectando con la API: " + baseUrl + entityPath + ". " + e.getMessage(), e);
    } catch (IllegalArgumentException e) {
      logger.error("Configuración inválida para la entidad: {}", entityPath, e);
      throw new RuntimeException("Configuración inválida para la entidad: " + entityPath + ". " + e.getMessage(), e);
    } catch (Exception e) {
      logger.error("Error inesperado al obtener entidades desde {}: {}", baseUrl + entityPath, e.getMessage(), e);
      throw new RuntimeException("Error inesperado al obtener entidades: " + e.getMessage(), e);
    }
  }

  public T findById(ID id) {
    if (id == null) {
      throw new IllegalArgumentException("ID no puede ser null");
    }
    try {
      String url = baseUrl + entityPath + "/" + id;
      logger.debug("Buscando {} con ID: {}", getEntityClass().getSimpleName(), id);
      T result = restTemplate.getForObject(url, getEntityClass());
      if (result != null) {
        logger.info("Entidad {} encontrada con ID: {}", getEntityClass().getSimpleName(), id);
      }
      return result;
    } catch (HttpClientErrorException.NotFound e) {
      logger.warn("No se encontró {} con ID: {}", getEntityClass().getSimpleName(), id);
      throw new EntityNotFoundException("No se encontró el recurso con ID: " + id);
    } catch (HttpServerErrorException e) {
      logger.error("Error del servidor al buscar {}: {}", getEntityClass().getSimpleName(), e.getMessage());
      throw new RuntimeException("Error en el servidor: " + e.getStatusCode() + " - " + e.getMessage(), e);
    } catch (ResourceAccessException e) {
      logger.error("Error de conexión con la API al buscar ID: {}", id);
      throw new RuntimeException("No se pudo conectar con la API", e);
    } catch (RestClientException e) {
      logger.error("Error al buscar {} con ID {}: {}", getEntityClass().getSimpleName(), id, e.getMessage());
      throw new RuntimeException("Error al buscar el recurso: " + e.getMessage(), e);
    }
  }

  public T save(T entity) {
    if (entity == null) {
      throw new IllegalArgumentException("La entidad no puede ser null");
    }
    try {
      String url = baseUrl + entityPath;
      logger.debug("Guardando nueva entidad {} en: {}", getEntityClass().getSimpleName(), url);
      T result = restTemplate.postForObject(url, entity, getEntityClass());
      logger.info("Entidad {} guardada exitosamente con ID: {}", getEntityClass().getSimpleName(), result != null ? result.getId() : "N/A");
      return result;
    } catch (HttpClientErrorException e) {
      logger.error("Error del cliente al guardar entidad: {} - {}", e.getStatusCode(), e.getMessage());
      throw new RuntimeException("Error al guardar: " + e.getStatusCode() + " - " + e.getMessage(), e);
    } catch (HttpServerErrorException e) {
      logger.error("Error del servidor al guardar entidad: {}", e.getMessage());
      throw new RuntimeException("Error en el servidor: " + e.getStatusCode() + " - " + e.getMessage(), e);
    } catch (ResourceAccessException e) {
      logger.error("Error de conexión con la API al guardar entidad");
      throw new RuntimeException("No se pudo conectar con la API", e);
    } catch (RestClientException e) {
      logger.error("Error al guardar entidad: {}", e.getMessage());
      throw new RuntimeException("Error al guardar el recurso: " + e.getMessage(), e);
    }
  }

  public T update(ID id, T entity) {
    if (id == null) {
      throw new IllegalArgumentException("ID no puede ser null");
    }
    if (entity == null) {
      throw new IllegalArgumentException("La entidad no puede ser null");
    }
    try {
      String url = baseUrl + entityPath + "/" + id;
      logger.debug("Actualizando {} con ID: {}", getEntityClass().getSimpleName(), id);
      HttpEntity<T> requestEntity = new HttpEntity<>(entity);
      ResponseEntity<T> response = restTemplate.exchange(
        url, 
        HttpMethod.PUT, 
        requestEntity, 
        getEntityClass()
      );
      logger.info("Entidad {} actualizada exitosamente con ID: {}", getEntityClass().getSimpleName(), id);
      return response.getBody();
    } catch (HttpClientErrorException.NotFound e) {
      logger.warn("No se encontró {} con ID: {} para actualizar", getEntityClass().getSimpleName(), id);
      throw new EntityNotFoundException("No se encontró el recurso con ID: " + id);
    } catch (HttpClientErrorException e) {
      logger.error("Error del cliente al actualizar entidad: {} - {}", e.getStatusCode(), e.getMessage());
      throw new RuntimeException("Error al actualizar: " + e.getStatusCode() + " - " + e.getMessage(), e);
    } catch (HttpServerErrorException e) {
      logger.error("Error del servidor al actualizar {}: {}", getEntityClass().getSimpleName(), e.getMessage());
      throw new RuntimeException("Error en el servidor: " + e.getStatusCode() + " - " + e.getMessage(), e);
    } catch (ResourceAccessException e) {
      logger.error("Error de conexión con la API al actualizar ID: {}", id);
      throw new RuntimeException("No se pudo conectar con la API", e);
    } catch (RestClientException e) {
      logger.error("Error al actualizar {} con ID {}: {}", getEntityClass().getSimpleName(), id, e.getMessage());
      throw new RuntimeException("Error al actualizar el recurso: " + e.getMessage(), e);
    }
  }

  public void delete(ID id) {
    if (id == null) {
      throw new IllegalArgumentException("ID no puede ser null");
    }
    try {
      String url = baseUrl + entityPath + "/" + id;
      logger.debug("Eliminando {} con ID: {}", getEntityClass().getSimpleName(), id);
      restTemplate.delete(url);
      logger.info("Entidad {} eliminada exitosamente con ID: {}", getEntityClass().getSimpleName(), id);
    } catch (HttpClientErrorException.NotFound e) {
      logger.warn("No se encontró {} con ID: {} para eliminar", getEntityClass().getSimpleName(), id);
      throw new EntityNotFoundException("No se encontró el recurso con ID: " + id);
    } catch (HttpServerErrorException e) {
      logger.error("Error del servidor al eliminar {}: {}", getEntityClass().getSimpleName(), e.getMessage());
      throw new RuntimeException("Error en el servidor: " + e.getStatusCode() + " - " + e.getMessage(), e);
    } catch (ResourceAccessException e) {
      logger.error("Error de conexión con la API al eliminar ID: {}", id);
      throw new RuntimeException("No se pudo conectar con la API", e);
    } catch (RestClientException e) {
      logger.error("Error al eliminar {} con ID {}: {}", getEntityClass().getSimpleName(), id, e.getMessage());
      throw new RuntimeException("Error conectando con la API: " + baseUrl + entityPath + "/" + id + ". " + e.getMessage(), e);
    } catch (IllegalArgumentException e) {
      logger.error("Configuración inválida para la entidad: {}", entityPath);
      throw new RuntimeException("Configuración inválida para la entidad: " + entityPath + ". " + e.getMessage(), e);
    }
  }

  protected abstract Class<T> getEntityClass();

  @SuppressWarnings("unchecked")
  private Class<T[]> getArrayClass() {
    return (Class<T[]>) Array.newInstance(getEntityClass(), 0).getClass();
  }

}