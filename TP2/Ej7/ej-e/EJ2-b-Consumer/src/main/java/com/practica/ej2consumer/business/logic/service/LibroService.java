package com.practica.ej2consumer.business.logic.service;

import org.springframework.stereotype.Service;

import com.practica.ej2consumer.business.domain.dto.LibroDTO;
import com.practica.ej2consumer.business.persistence.rest.LibroDAORest;


@Service
public class LibroService extends BaseService<LibroDTO, Long> {

  public LibroService(LibroDAORest dao) {
    super(dao);
  }

  @Override
  protected void validateEntity(LibroDTO entity) throws Exception {
    if (entity.getTitulo() == null || entity.getTitulo().isEmpty()) {
      throw new Exception("El título no puede ser nulo o vacío");
    }
    if (entity.getGenero() == null || entity.getGenero().isEmpty()) {
      throw new Exception("El género no puede ser nulo o vacío");
    }
    if (entity.getPaginas() <= 0) {
      throw new Exception("El número de páginas debe ser mayor que cero");
    }
    if (entity.getFecha() == null) {
      throw new Exception("La fecha no puede ser nula");
    }
    if (entity.getAutores() == null || entity.getAutores().isEmpty()) {
      throw new Exception("El libro debe tener al menos un autor");
    }
    
  }

}
