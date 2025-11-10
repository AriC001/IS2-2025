package com.nexora.proyecto.gestion.business.persistence.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.nexora.proyecto.gestion.dto.ClienteDTO;

@Component
public class ClienteDAO extends BaseDAO<ClienteDTO, String> {

  @Autowired
  public ClienteDAO(RestTemplate restTemplate) {
    super(restTemplate, "/api/v1/clientes");
  }

  @Override
  protected Class<ClienteDTO> getEntityClass() {
    return ClienteDTO.class;
  }

}

