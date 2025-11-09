package com.nexora.proyecto.gestion.business.persistence.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.nexora.proyecto.gestion.dto.LocalidadDTO;

@Component
public class LocalidadDAO extends BaseDAO<LocalidadDTO, String> {

  @Autowired
  public LocalidadDAO(RestTemplate restTemplate) {
    super(restTemplate, "/api/v1/localidades");
  }

  @Override
  protected Class<LocalidadDTO> getEntityClass() {
    return LocalidadDTO.class;
  }
}
