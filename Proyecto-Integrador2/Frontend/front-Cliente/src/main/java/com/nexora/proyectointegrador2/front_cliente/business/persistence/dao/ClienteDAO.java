package com.nexora.proyectointegrador2.front_cliente.business.persistence.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.nexora.proyectointegrador2.front_cliente.dto.ClienteDTO;

@Component
public class ClienteDAO extends BaseDAO<ClienteDTO, String> {

  private static final Logger logger = LoggerFactory.getLogger(ClienteDAO.class);

  @Autowired
  public ClienteDAO(RestTemplate restTemplate) {
    super(restTemplate, "/clientes");
  }

  @Override
  protected Class<ClienteDTO> getEntityClass() {
    return ClienteDTO.class;
  }
  

  public ClienteDTO findByNombreUsuario(String nombreUsuario) {
    try {
      String url = baseUrl + entityPath + "/por-usuario/" + nombreUsuario;
      logger.debug("Buscando cliente por nombreUsuario: {} desde URL: {}", nombreUsuario, url);
      ClienteDTO result = restTemplate.getForObject(url, ClienteDTO.class);
      if (result != null) {
        logger.info("Cliente encontrado para nombreUsuario: {}", nombreUsuario);
      } else {
        logger.warn("No se encontró cliente para nombreUsuario: {}", nombreUsuario);
      }
      return result;
    } catch (HttpClientErrorException.NotFound e) {
      logger.warn("No se encontró cliente con nombreUsuario: {}", nombreUsuario);
      return null; // Retornar null en lugar de lanzar excepción
    } catch (HttpClientErrorException e) {
      logger.error("Error del cliente al buscar por nombreUsuario {}: {} - {}", nombreUsuario, e.getStatusCode(), e.getMessage());
      throw new RuntimeException("Error buscando cliente por nombreUsuario: " + e.getStatusCode() + " - " + e.getMessage(), e);
    } catch (HttpServerErrorException e) {
      logger.error("Error del servidor al buscar cliente por nombreUsuario {}: {}", nombreUsuario, e.getMessage());
      throw new RuntimeException("Error buscando cliente por nombreUsuario: " + e.getStatusCode() + " - " + e.getMessage(), e);
    } catch (ResourceAccessException e) {
      logger.error("Error de conexión con la API al buscar cliente por nombreUsuario: {}", nombreUsuario);
      throw new RuntimeException("Error buscando cliente por nombreUsuario: No se pudo conectar con la API", e);
    } catch (RestClientException e) {
      logger.error("Error al buscar cliente por nombreUsuario {}: {}", nombreUsuario, e.getMessage());
      throw new RuntimeException("Error buscando cliente por nombreUsuario: " + e.getMessage(), e);
    }
  }
}

