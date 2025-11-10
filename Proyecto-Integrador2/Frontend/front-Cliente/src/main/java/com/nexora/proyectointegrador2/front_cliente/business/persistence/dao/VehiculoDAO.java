package com.nexora.proyectointegrador2.front_cliente.business.persistence.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.nexora.proyectointegrador2.front_cliente.dto.VehiculoDTO;

@Component
public class VehiculoDAO extends BaseDAO<VehiculoDTO, String> {

  @Autowired
  public VehiculoDAO(RestTemplate restTemplate) {
    super(restTemplate, "/vehiculos");
  }

  @Override
  protected Class<VehiculoDTO> getEntityClass() {
    return VehiculoDTO.class;
  }

}

