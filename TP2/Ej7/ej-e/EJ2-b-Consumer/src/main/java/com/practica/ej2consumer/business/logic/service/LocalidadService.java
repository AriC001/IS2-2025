package com.practica.ej2consumer.business.logic.service;

import org.springframework.stereotype.Service;

import com.practica.ej2consumer.business.domain.dto.LocalidadDTO;
import com.practica.ej2consumer.business.persistence.rest.LocalidadDAORest;

@Service
public class LocalidadService extends BaseService<LocalidadDTO, Long> {

  public LocalidadService(LocalidadDAORest dao) {
    super(dao);
  }

  @Override
  protected void validateEntity(LocalidadDTO entity) throws Exception {
    if (entity.getNombre() == null || entity.getNombre().isEmpty()) {
      throw new Exception("El nombre de la localidad no puede ser nulo o vacío");
    }
  }

}
