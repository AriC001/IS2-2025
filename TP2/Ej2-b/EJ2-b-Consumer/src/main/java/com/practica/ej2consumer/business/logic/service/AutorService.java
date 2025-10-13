package com.practica.ej2consumer.business.logic.service;

import org.springframework.stereotype.Service;

import com.practica.ej2consumer.business.domain.dto.AutorDTO;
import com.practica.ej2consumer.business.persistence.rest.AutorDAORest;

@Service
public class AutorService extends BaseService<AutorDTO, Long> {

  public AutorService(AutorDAORest dao) {
    super(dao);
  }

  @Override
  protected void validateEntity(AutorDTO entity) throws Exception {
    if (entity.getNombre() == null || entity.getNombre().isEmpty()) {
      throw new Exception("El nombre no puede ser nulo o vacío");
    }
    if (entity.getApellido() == null || entity.getApellido().isEmpty()) {
      throw new Exception("El apellido no puede ser nulo o vacío");
    }
  }

}
