package com.nexora.proyecto.gestion.business.persistence.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.nexora.proyecto.gestion.dto.CaracteristicaVehiculoDTO;

@Component
public class CaracteristicaVehiculoDAO extends BaseDAO<CaracteristicaVehiculoDTO, String> {

  @Autowired
  public CaracteristicaVehiculoDAO(RestTemplate restTemplate) {
    super(restTemplate, "/caracteristicas-vehiculo");
  }

  @Override
  protected Class<CaracteristicaVehiculoDTO> getEntityClass() {
    return CaracteristicaVehiculoDTO.class;
  }

}

