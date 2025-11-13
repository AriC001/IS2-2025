package com.nexora.proyectointegrador2.front_cliente.business.persistence.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.nexora.proyectointegrador2.front_cliente.dto.FacturaDTO;
import com.nexora.proyectointegrador2.front_cliente.exception.EntityNotFoundException;

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
    } catch (HttpServerErrorException e) {
      // Si el backend devuelve 500, puede ser por problemas de serialización o porque no existe la factura
      // En lugar de lanzar excepción, devolvemos null para que el controlador maneje el caso apropiadamente
      logger.warn("Error del servidor al obtener factura por alquiler {}: {}. Tratando como factura no encontrada.", alquilerId, e.getResponseBodyAsString());
      return null;
    } catch (HttpClientErrorException e) {
      logger.error("Error del cliente al obtener factura por alquiler: {} - {}", e.getStatusCode(), e.getMessage());
      throw new RuntimeException("Error al obtener factura: " + e.getStatusCode() + " - " + e.getMessage(), e);
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
    } catch (HttpClientErrorException.NotFound e) {
      logger.warn("PDF de factura no encontrado: {}", facturaId);
      throw new EntityNotFoundException("No se encontró el PDF de la factura solicitada. Es posible que la factura haya sido eliminada o el archivo no exista.");
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

