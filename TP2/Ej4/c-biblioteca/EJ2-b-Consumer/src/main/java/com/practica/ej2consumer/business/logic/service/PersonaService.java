package com.practica.ej2consumer.business.logic.service;

import org.springframework.stereotype.Service;

import com.practica.ej2consumer.business.domain.dto.PersonaDTO;
import com.practica.ej2consumer.business.persistence.rest.PersonaDAORest;


@Service
public class PersonaService extends BaseService<PersonaDTO, Long> {

  public PersonaService(PersonaDAORest dao) {
    super(dao);
  }

  @Override
  protected void validateEntity(PersonaDTO entity) throws Exception {
    if (entity.getNombre() == null || entity.getNombre().isEmpty()) {
      throw new Exception("El nombre no puede ser nulo o vacío");
    }
    if (entity.getApellido() == null || entity.getApellido().isEmpty()) {
      throw new Exception("El apellido no puede ser nulo o vacío");
    }
    if (entity.getDomicilio() == null) {
      throw new Exception("El domicilio no puede ser nulo");
    }
  }

}
