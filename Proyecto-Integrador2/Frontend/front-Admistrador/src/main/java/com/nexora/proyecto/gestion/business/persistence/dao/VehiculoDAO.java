package com.nexora.proyecto.gestion.business.persistence.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.nexora.proyecto.gestion.dto.VehiculoDTO;

@Component
public class VehiculoDAO extends BaseDAO<VehiculoDTO, String> {

  @Autowired
  public VehiculoDAO(RestTemplate restTemplate) {
    super(restTemplate, "/api/v1/vehiculos");
  }

  @Override
  protected Class<VehiculoDTO> getEntityClass() {
    return VehiculoDTO.class;
  }

}

