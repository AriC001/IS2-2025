package com.practica.ej2b.business.logic.service;

import org.springframework.stereotype.Service;

import com.practica.ej2b.business.domain.entity.Persona;
import com.practica.ej2b.business.persistence.repository.PersonaRepository;


@Service
public class PersonaService extends BaseService<Persona, Long> {

  public PersonaService(PersonaRepository repository) {
    super(repository);
  }

  @Override
  protected void validar(Persona entity) throws Exception {
    if (entity.getNombre() == null || entity.getNombre().isEmpty()) {
      throw new Exception("El nombre no puede ser nulo o vacío"); 
    }
    if (entity.getApellido() == null || entity.getApellido().isEmpty()) {
      throw new Exception("El apellido no puede ser nulo o vacío");
    }
    if (entity.getDni() == null || entity.getDni().isEmpty()) {
      throw new Exception("El DNI no puede ser nulo o vacío");
    }
    if (entity.getDni().length() != 8) {
      throw new Exception("El DNI debe tener 8 caracteres");
    }
    // domicilio can be optional when registering from the consumer UI
    // if you require domicilio for other flows, validate it elsewhere
  }


  
}
