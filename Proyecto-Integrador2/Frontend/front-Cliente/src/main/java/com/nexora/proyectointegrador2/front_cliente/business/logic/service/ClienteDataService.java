package com.nexora.proyectointegrador2.front_cliente.business.logic.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.nexora.proyectointegrador2.front_cliente.dto.LocalidadDTO;
import com.nexora.proyectointegrador2.front_cliente.dto.NacionalidadDTO;

@Service
public class ClienteDataService {

  private static final Logger logger = LoggerFactory.getLogger(ClienteDataService.class);

  private final RestTemplate restTemplate;

  @Value("${api.base.url}")
  private String baseUrl;

  public ClienteDataService(RestTemplate restTemplate) {
    this.restTemplate = restTemplate;
  }

  public List<NacionalidadDTO> obtenerNacionalidades() throws Exception {
    try {
      String url = baseUrl + "/nacionalidades";
      
      HttpHeaders headers = new HttpHeaders();
      HttpEntity<Void> request = new HttpEntity<>(headers);
      
      logger.debug("Obteniendo nacionalidades desde: {}", url);
      
      ResponseEntity<List<NacionalidadDTO>> response = restTemplate.exchange(
          url,
          HttpMethod.GET,
          request,
          new ParameterizedTypeReference<List<NacionalidadDTO>>() {}
      );
      
      return response.getBody();
      
    } catch (Exception e) {
      logger.error("Error al obtener nacionalidades: {}", e.getMessage());
      throw new Exception("Error al cargar nacionalidades");
    }
  }

  public List<LocalidadDTO> obtenerLocalidades() throws Exception {
    try {
      String url = baseUrl + "/localidades";
      
      HttpHeaders headers = new HttpHeaders();
      HttpEntity<Void> request = new HttpEntity<>(headers);
      
      logger.debug("Obteniendo localidades desde: {}", url);
      
      ResponseEntity<List<LocalidadDTO>> response = restTemplate.exchange(
          url,
          HttpMethod.GET,
          request,
          new ParameterizedTypeReference<List<LocalidadDTO>>() {}
      );
      
      return response.getBody();
      
    } catch (Exception e) {
      logger.error("Error al obtener localidades: {}", e.getMessage());
      throw new Exception("Error al cargar localidades");
    }
  }
}