package com.nexora.proyecto.gestion.business.persistence.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.nexora.proyecto.gestion.dto.FacturaDTO;
import com.nexora.proyecto.gestion.dto.enums.TipoPago;

@Component
public class FacturaDAO extends BaseDAO<FacturaDTO, String> {

  @Autowired
  public FacturaDAO(RestTemplate restTemplate) {
    super(restTemplate, "/facturas");
  }

  @Override
  protected Class<FacturaDTO> getEntityClass() {
    return FacturaDTO.class;
  }

  /**
   * Genera una factura para un alquiler.
   */
  public FacturaDTO generarFactura(String alquilerId, TipoPago tipoPago, String observacion) {
    try {
      StringBuilder urlBuilder = new StringBuilder(baseUrl + entityPath + "/generar");
      urlBuilder.append("?alquilerId=").append(alquilerId);
      urlBuilder.append("&tipoPago=").append(tipoPago != null ? tipoPago.name() : "EFECTIVO");
      if (observacion != null && !observacion.trim().isEmpty()) {
        urlBuilder.append("&observacion=").append(java.net.URLEncoder.encode(observacion, "UTF-8"));
      }
      String url = urlBuilder.toString();
      
      logger.debug("Generando factura para alquiler: {}", alquilerId);

      HttpHeaders headers = new HttpHeaders();
      HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

      ResponseEntity<FacturaDTO> response = restTemplate.exchange(
          url,
          HttpMethod.POST,
          requestEntity,
          FacturaDTO.class
      );

      logger.info("Factura generada exitosamente para alquiler: {}", alquilerId);
      return response.getBody();
    } catch (java.io.UnsupportedEncodingException e) {
      logger.error("Error al codificar parámetros: {}", e.getMessage());
      throw new RuntimeException("Error al codificar parámetros: " + e.getMessage(), e);
    } catch (HttpClientErrorException e) {
      logger.error("Error del cliente al generar factura: {} - {}", e.getStatusCode(), e.getMessage());
      throw new RuntimeException("Error al generar factura: " + e.getStatusCode() + " - " + e.getMessage(), e);
    } catch (HttpServerErrorException e) {
      logger.error("Error del servidor al generar factura: {}", e.getMessage());
      throw new RuntimeException("Error en el servidor: " + e.getStatusCode() + " - " + e.getMessage(), e);
    } catch (ResourceAccessException e) {
      logger.error("Error de conexión con la API al generar factura");
      throw new RuntimeException("No se pudo conectar con la API", e);
    } catch (RestClientException e) {
      logger.error("Error al generar factura: {}", e.getMessage());
      throw new RuntimeException("Error al generar factura: " + e.getMessage(), e);
    }
  }

  /**
   * Obtiene una factura por el ID del alquiler.
   */
  public FacturaDTO findByAlquilerId(String alquilerId) {
    try {
      String url = baseUrl + entityPath + "/alquiler/" + alquilerId;
      logger.debug("Obteniendo factura para alquiler: {}", alquilerId);

      ResponseEntity<FacturaDTO> response = restTemplate.getForEntity(url, FacturaDTO.class);
      return response.getBody();
    } catch (HttpClientErrorException.NotFound e) {
      logger.debug("No se encontró factura para alquiler: {}", alquilerId);
      return null;
    } catch (HttpClientErrorException e) {
      logger.error("Error del cliente al obtener factura por alquiler: {} - {}", e.getStatusCode(), e.getMessage());
      throw new RuntimeException("Error al obtener factura: " + e.getStatusCode() + " - " + e.getMessage(), e);
    } catch (HttpServerErrorException e) {
      logger.error("Error del servidor al obtener factura por alquiler: {}", e.getMessage());
      throw new RuntimeException("Error en el servidor: " + e.getStatusCode() + " - " + e.getMessage(), e);
    } catch (ResourceAccessException e) {
      logger.error("Error de conexión con la API al obtener factura por alquiler");
      throw new RuntimeException("No se pudo conectar con la API", e);
    } catch (RestClientException e) {
      logger.error("Error al obtener factura por alquiler: {}", e.getMessage());
      throw new RuntimeException("Error al obtener factura: " + e.getMessage(), e);
    }
  }

  /**
   * Descarga el PDF de una factura.
   */
  public byte[] descargarPdf(String facturaId) {
    try {
      String url = baseUrl + entityPath + "/" + facturaId + "/pdf";
      logger.debug("Descargando PDF de factura: {}", facturaId);

      ResponseEntity<byte[]> response = restTemplate.getForEntity(url, byte[].class);
      return response.getBody();
    } catch (HttpClientErrorException e) {
      logger.error("Error del cliente al descargar PDF: {} - {}", e.getStatusCode(), e.getMessage());
      throw new RuntimeException("Error al descargar PDF: " + e.getStatusCode() + " - " + e.getMessage(), e);
    } catch (HttpServerErrorException e) {
      logger.error("Error del servidor al descargar PDF: {}", e.getMessage());
      throw new RuntimeException("Error en el servidor: " + e.getStatusCode() + " - " + e.getMessage(), e);
    } catch (ResourceAccessException e) {
      logger.error("Error de conexión con la API al descargar PDF");
      throw new RuntimeException("No se pudo conectar con la API", e);
    } catch (RestClientException e) {
      logger.error("Error al descargar PDF: {}", e.getMessage());
      throw new RuntimeException("Error al descargar PDF: " + e.getMessage(), e);
    }
  }

}

