package com.nexora.proyecto.gestion.business.persistence.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.nexora.proyecto.gestion.dto.ProvinciaDTO;

@Component
public class ProvinciaDAO extends BaseDAO<ProvinciaDTO, String> {
  @Autowired
  public ProvinciaDAO(RestTemplate restTemplate) {
    super(restTemplate, "/api/v1/provincias");
  }

  @Override
  protected Class<ProvinciaDTO> getEntityClass() {
    return ProvinciaDTO.class;
  }

}
