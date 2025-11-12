package com.nexora.proyectointegrador2.front_cliente.business.persistence.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.nexora.proyectointegrador2.front_cliente.dto.DireccionDTO;

@Component
public class DireccionDAO extends BaseDAO<DireccionDTO, String> {

  @Autowired
  public DireccionDAO(RestTemplate restTemplate) {
    super(restTemplate, "/direcciones");
  }

  @Override
  protected Class<DireccionDTO> getEntityClass() {
    return DireccionDTO.class;
  }
}

