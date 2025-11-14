package com.nexora.proyectointegrador2.front_cliente.business.persistence.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.nexora.proyectointegrador2.front_cliente.dto.ClienteDTO;
import org.springframework.web.util.UriComponentsBuilder;

@Component
public class ClienteDAO extends BaseDAO<ClienteDTO, String> {

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
            String url = baseUrl + "/por-usuario/" + nombreUsuario;
            return restTemplate.getForObject(url, ClienteDTO.class);
        } catch (Exception e) {
            throw new RuntimeException("Error buscando cliente por nombreUsuario: " + e.getMessage());
        }
    }


    public ClienteDTO findByIdUsuario(String idUsuario) {
        try {
            String url = UriComponentsBuilder
                    .fromHttpUrl(baseUrl + "/clientes/por-usuario")
                    .queryParam("idUsuario", idUsuario)
                    .toUriString();

            return restTemplate.getForObject(url, ClienteDTO.class);

        } catch (Exception e) {
            throw new RuntimeException("Error buscando cliente por Usuario: " + e.getMessage());
        }
    }
}

