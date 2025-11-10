package com.practica.nexora.ej6_e.business.logic.service;

import org.springframework.stereotype.Service;

import com.practica.nexora.ej6_e.business.domain.entity.Persona;
import com.practica.nexora.ej6_e.business.persistence.repository.PersonaRepository;

@Service
public class PersonaService extends BaseService<Persona, Long> {

  private final PersonaRepository personaRepository;

  public PersonaService(PersonaRepository personaRepository) {
    super(personaRepository);
    this.personaRepository = personaRepository;
  }

  @Override
  protected void validate(Persona entity) throws IllegalArgumentException {
    if (entity == null) {
      throw new IllegalArgumentException("La Persona no puede ser nula");
    }
    // Additional validation logic can be added here
    if (entity.getNombre() == null || entity.getNombre().isEmpty()) {
      throw new IllegalArgumentException("El nombre de la persona no puede ser nulo o vacio");
    }
    if (entity.getApellido() == null || entity.getApellido().isEmpty()) {
      throw new IllegalArgumentException("El apellido de la persona no puede ser nulo o vacio");
    }
  }

}
