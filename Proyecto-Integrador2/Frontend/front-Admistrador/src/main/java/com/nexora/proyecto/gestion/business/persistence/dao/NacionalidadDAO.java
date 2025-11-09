package com.nexora.proyecto.gestion.business.persistence.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.nexora.proyecto.gestion.dto.NacionalidadDTO;

@Component
public class NacionalidadDAO extends BaseDAO<NacionalidadDTO, String> { 

  @Autowired
  public NacionalidadDAO(RestTemplate restTemplate) {
    super(restTemplate, "/api/v1/nacionalidades");
  }

  @Override
  protected Class<NacionalidadDTO> getEntityClass() {
    return NacionalidadDTO.class;
  }

}
