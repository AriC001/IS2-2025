package com.nexora.proyectointegrador2.front_cliente.business.persistence.dao;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.StringJoiner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.nexora.proyectointegrador2.front_cliente.dto.VehiculoDTO;

@Component
public class VehiculoDAO extends BaseDAO<VehiculoDTO, String> {

  private static final Logger logger = LoggerFactory.getLogger(VehiculoDAO.class);

  @Autowired
  public VehiculoDAO(RestTemplate restTemplate) {
    super(restTemplate, "/vehiculos");
  }

  @Override
  protected Class<VehiculoDTO> getEntityClass() {
    return VehiculoDTO.class;
  }

  public boolean findAvailability(Map<String,String> param) {
    try {
      String url = baseUrl + entityPath + "/availability";
      StringJoiner sj = new StringJoiner("&");
      for (Map.Entry<String, String> e : param.entrySet()) {
        if (e.getValue() != null && !e.getValue().isBlank()) {
          sj.add(URLEncoder.encode(e.getKey(), StandardCharsets.UTF_8) + "=" + URLEncoder.encode(e.getValue(), StandardCharsets.UTF_8));
        }
      }
      String q = sj.toString();
      if (!q.isBlank()) {
        url = url + "?" + q;
      }
      
      logger.debug("Llamando al backend para verificar disponibilidad: {}", url);
      
      // Call backend which returns a boolean indicating availability for the given params.
      // Use Boolean.class to let RestTemplate map the response; handle null safely.
      Boolean resp = restTemplate.getForObject(url, Boolean.class);
      boolean result = Boolean.TRUE.equals(resp);
      logger.debug("Respuesta del backend: {}", result);
      return result;
    } catch (org.springframework.web.client.HttpClientErrorException e) {
      logger.error("Error del cliente al verificar disponibilidad: {} - {}", e.getStatusCode(), e.getMessage());
      throw new RuntimeException("Error al verificar disponibilidad: " + e.getStatusCode() + " - " + e.getMessage(), e);
    } catch (org.springframework.web.client.HttpServerErrorException e) {
      logger.error("Error del servidor al verificar disponibilidad: {} - {}", e.getStatusCode(), e.getMessage());
      throw new RuntimeException("Error del servidor al verificar disponibilidad: " + e.getStatusCode() + " - " + e.getMessage(), e);
    } catch (Exception e) {
      logger.error("Error al verificar disponibilidad: {}", e.getMessage(), e);
      throw new RuntimeException("Error al verificar disponibilidad: " + e.getMessage(), e);
    }
  }

}

