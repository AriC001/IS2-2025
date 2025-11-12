package com.nexora.proyectointegrador2.front_cliente.business.persistence.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.nexora.proyectointegrador2.front_cliente.dto.LocalidadDTO;

@Component
public class LocalidadDAO extends BaseDAO<LocalidadDTO, String> {

  @Autowired
  public LocalidadDAO(RestTemplate restTemplate) {
    super(restTemplate, "/localidades");
  }

  @Override
  protected Class<LocalidadDTO> getEntityClass() {
    return LocalidadDTO.class;
  }
}
