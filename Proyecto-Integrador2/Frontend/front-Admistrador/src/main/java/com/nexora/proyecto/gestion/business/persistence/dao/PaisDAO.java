package com.nexora.proyecto.gestion.business.persistence.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.nexora.proyecto.gestion.dto.PaisDTO;

@Component
public class PaisDAO extends BaseDAO<PaisDTO, String> {
  @Autowired
  public PaisDAO(RestTemplate restTemplate) {
    super(restTemplate, "/api/v1/paises");
  }

  @Override
  protected Class<PaisDTO> getEntityClass() {
    return PaisDTO.class;
  }

}
