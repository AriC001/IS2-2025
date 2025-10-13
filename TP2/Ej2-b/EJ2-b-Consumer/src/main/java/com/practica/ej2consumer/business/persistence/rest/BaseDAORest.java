package com.practica.ej2consumer.business.persistence.rest;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.practica.ej2consumer.business.domain.dto.BaseDTO;

@Component
public abstract class BaseDAORest<T extends BaseDTO, ID> {
    
  @Autowired
  protected RestTemplate restTemplate;

  @Value("${api.base.url}")
  protected String baseUrl;

  protected String entityPath;

  public BaseDAORest(RestTemplate restTemplate,
                    String entityPath) {
    this.restTemplate = restTemplate;
    this.entityPath = entityPath;
  }

  public List<T> findAllActives() {
    try {
      String url = baseUrl + entityPath;
      ResponseEntity<T[]> response = restTemplate.getForEntity(url, getArrayClass());
      T[] body = response.getBody();
      return body != null ? Arrays.asList(body) : List.of();
    } catch (RestClientException e) {
      throw new RuntimeException("Error conectando con la API: " + baseUrl + entityPath + ". " + e.getMessage(), e);
    } catch (IllegalArgumentException e) {
      throw new RuntimeException("Configuración inválida para la entidad: " + entityPath + ". " + e.getMessage(), e);
    }
  }

  public T findById(ID id) {
    String url = baseUrl + entityPath + "/" + id;
    return restTemplate.getForObject(url, getEntityClass());
  }

  public T save(T entity) {
    String url = baseUrl + entityPath;
    return restTemplate.postForObject(url, entity, getEntityClass());
  }

  public T update(ID id, T entity) {
    String url = baseUrl + entityPath + "/" + id;
    HttpEntity<T> requestEntity = new HttpEntity<>(entity);
    ResponseEntity<T> response = restTemplate.exchange(
      url, 
      HttpMethod.PUT, 
      requestEntity, 
      getEntityClass()
    );
    return response.getBody();
  }

  public void delete(ID id) {
    try {
      String url = baseUrl + entityPath + "/" + id;
      restTemplate.delete(url);
    } catch (RestClientException e) {
      throw new RuntimeException("Error conectando con la API: " + baseUrl + entityPath + "/" + id + ". " + e.getMessage(), e);
    } catch (IllegalArgumentException e) {
      throw new RuntimeException("Configuración inválida para la entidad: " + entityPath + ". " + e.getMessage(), e);
    }
  }

  protected abstract Class<T> getEntityClass();

  @SuppressWarnings("unchecked")
  private Class<T[]> getArrayClass() {
    return (Class<T[]>) Array.newInstance(getEntityClass(), 0).getClass();
  }

}
