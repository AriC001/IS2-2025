package com.nexora.proyecto.gestion.business.persistence.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.nexora.proyecto.gestion.dto.AlquilerDTO;

@Component
public class AlquilerDAO extends BaseDAO<AlquilerDTO, String> {

  @Autowired
  public AlquilerDAO(RestTemplate restTemplate) {
    super(restTemplate, "/alquileres");
  }

  @Override
  protected Class<AlquilerDTO> getEntityClass() {
    return AlquilerDTO.class;
  }

}

