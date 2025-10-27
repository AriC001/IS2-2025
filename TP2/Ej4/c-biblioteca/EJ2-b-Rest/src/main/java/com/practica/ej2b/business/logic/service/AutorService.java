package com.practica.ej2b.business.logic.service;

import org.springframework.stereotype.Service;

import com.practica.ej2b.business.domain.entity.Autor;
import com.practica.ej2b.business.persistence.repository.AutorRepository;

@Service
public class AutorService extends BaseService<Autor, Long> {

  public AutorService(AutorRepository repository) {
    super(repository);
  }

  @Override
  protected void validar(Autor entity) throws Exception {
    if (entity.getNombre() == null || entity.getNombre().isEmpty()) {
      throw new Exception("El nombre no puede ser nulo o vacío"); 
    }
    if (entity.getApellido() == null || entity.getApellido().isEmpty()) {
      throw new Exception("El apellido no puede ser nulo o vacío");
    }
  }

}
