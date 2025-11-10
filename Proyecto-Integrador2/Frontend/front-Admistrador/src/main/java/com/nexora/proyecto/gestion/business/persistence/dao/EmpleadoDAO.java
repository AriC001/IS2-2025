package com.nexora.proyecto.gestion.business.persistence.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.nexora.proyecto.gestion.dto.EmpleadoDTO;

@Component
public class EmpleadoDAO extends BaseDAO<EmpleadoDTO, String> {

  @Autowired
  public EmpleadoDAO(RestTemplate restTemplate) {
    super(restTemplate, "/empleados");
  }

  @Override
  protected Class<EmpleadoDTO> getEntityClass() {
    return EmpleadoDTO.class;
  }

}

