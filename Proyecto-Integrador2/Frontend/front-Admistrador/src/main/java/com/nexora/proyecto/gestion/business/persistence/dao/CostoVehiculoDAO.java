package com.nexora.proyecto.gestion.business.persistence.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.nexora.proyecto.gestion.dto.CostoVehiculoDTO;

@Component
public class CostoVehiculoDAO extends BaseDAO<CostoVehiculoDTO, String> {

  @Autowired
  public CostoVehiculoDAO(RestTemplate restTemplate) {
    super(restTemplate, "/costos-vehiculo");
  }

  @Override
  protected Class<CostoVehiculoDTO> getEntityClass() {
    return CostoVehiculoDTO.class;
  }

}

