package com.practica.ej2b.business.logic.service;

import org.springframework.stereotype.Service;

import com.practica.ej2b.business.domain.entity.Localidad;
import com.practica.ej2b.business.persistence.repository.LocalidadRepository;

@Service
public class LocalidadService extends BaseService<Localidad, Long> {

  public LocalidadService(LocalidadRepository repository) {
    super(repository);
  }

  @Override
  protected void validar(Localidad entity) throws Exception {
    if (entity.getNombre() == null || entity.getNombre().isEmpty()) {
      throw new Exception("El nombre no puede ser nulo o vacío"); 
    }
  }

}
