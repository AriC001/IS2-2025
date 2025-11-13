package com.nexora.proyecto.gestion.business.persistence.dao;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.nexora.proyecto.gestion.dto.ReporteAlquilerDTO;
import com.nexora.proyecto.gestion.dto.ReporteRecaudacionDTO;

@Component
public class ReporteDAO {

  private static final Logger logger = LoggerFactory.getLogger(ReporteDAO.class);
  private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

  @Autowired
  private RestTemplate restTemplate;

  @Value("${api.base.url}")
  private String baseUrl;

  /**
   * Obtiene el reporte de alquileres con factura.
   */
  public List<ReporteAlquilerDTO> obtenerAlquileresConFactura(Date fechaDesde, Date fechaHasta) {
    try {
      UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(baseUrl + "/reportes/alquileres-con-factura");
      
      if (fechaDesde != null) {
        builder.queryParam("fechaDesde", dateFormat.format(fechaDesde));
      }
      if (fechaHasta != null) {
        builder.queryParam("fechaHasta", dateFormat.format(fechaHasta));
      }

      String url = builder.toUriString();
      logger.debug("Obteniendo reporte de alquileres con factura desde: {}", url);

      ResponseEntity<ReporteAlquilerDTO[]> response = restTemplate.exchange(
          url,
          HttpMethod.GET,
          null,
          new ParameterizedTypeReference<ReporteAlquilerDTO[]>() {}
      );

      ReporteAlquilerDTO[] body = response.getBody();
      List<ReporteAlquilerDTO> result = body != null ? Arrays.asList(body) : List.of();
      logger.info("Se obtuvieron {} registros del reporte de alquileres con factura", result.size());
      return result;
    } catch (HttpClientErrorException e) {
      logger.error("Error del cliente (HTTP {}) al obtener reporte de alquileres: {}", 
          e.getStatusCode(), e.getMessage());
      throw new RuntimeException("Error al obtener reporte: " + e.getStatusCode() + " - " + e.getMessage(), e);
    } catch (HttpServerErrorException e) {
      logger.error("Error del servidor (HTTP {}) al obtener reporte de alquileres: {}", 
          e.getStatusCode(), e.getMessage());
      throw new RuntimeException("Error del servidor: " + e.getStatusCode() + " - " + e.getMessage(), e);
    } catch (ResourceAccessException e) {
      logger.error("Error de conexión con la API: {}", e.getMessage());
      throw new RuntimeException("No se pudo conectar con la API", e);
    } catch (RestClientException e) {
      logger.error("Error inesperado al obtener reporte de alquileres: {}", e.getMessage(), e);
      throw new RuntimeException("Error inesperado: " + e.getMessage(), e);
    }
  }

  /**
   * Obtiene el reporte de recaudación por modelo.
   */
  public List<ReporteRecaudacionDTO> obtenerRecaudacionPorModelo(Date fechaDesde, Date fechaHasta) {
    try {
      if (fechaDesde == null || fechaHasta == null) {
        throw new IllegalArgumentException("Las fechas desde y hasta son obligatorias");
      }

      UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(baseUrl + "/reportes/recaudacion-por-modelo")
          .queryParam("fechaDesde", dateFormat.format(fechaDesde))
          .queryParam("fechaHasta", dateFormat.format(fechaHasta));

      String url = builder.toUriString();
      logger.debug("Obteniendo reporte de recaudación por modelo desde: {}", url);

      ResponseEntity<ReporteRecaudacionDTO[]> response = restTemplate.exchange(
          url,
          HttpMethod.GET,
          null,
          new ParameterizedTypeReference<ReporteRecaudacionDTO[]>() {}
      );

      ReporteRecaudacionDTO[] body = response.getBody();
      List<ReporteRecaudacionDTO> result = body != null ? Arrays.asList(body) : List.of();
      logger.info("Se obtuvieron {} registros del reporte de recaudación por modelo", result.size());
      return result;
    } catch (HttpClientErrorException e) {
      logger.error("Error del cliente (HTTP {}) al obtener reporte de recaudación: {}", 
          e.getStatusCode(), e.getMessage());
      throw new RuntimeException("Error al obtener reporte: " + e.getStatusCode() + " - " + e.getMessage(), e);
    } catch (HttpServerErrorException e) {
      logger.error("Error del servidor (HTTP {}) al obtener reporte de recaudación: {}", 
          e.getStatusCode(), e.getMessage());
      throw new RuntimeException("Error del servidor: " + e.getStatusCode() + " - " + e.getMessage(), e);
    } catch (ResourceAccessException e) {
      logger.error("Error de conexión con la API: {}", e.getMessage());
      throw new RuntimeException("No se pudo conectar con la API", e);
    } catch (RestClientException e) {
      logger.error("Error inesperado al obtener reporte de recaudación: {}", e.getMessage(), e);
      throw new RuntimeException("Error inesperado: " + e.getMessage(), e);
    }
  }
}

