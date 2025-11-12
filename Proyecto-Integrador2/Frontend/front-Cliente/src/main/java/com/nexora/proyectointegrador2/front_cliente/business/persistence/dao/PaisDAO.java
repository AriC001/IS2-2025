package com.nexora.proyectointegrador2.front_cliente.business.persistence.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.nexora.proyectointegrador2.front_cliente.dto.PaisDTO;

@Component
public class PaisDAO extends BaseDAO<PaisDTO, String> {
  @Autowired
  public PaisDAO(RestTemplate restTemplate) {
    super(restTemplate, "/paises");
  }

  @Override
  protected Class<PaisDTO> getEntityClass() {
    return PaisDTO.class;
  }

}
