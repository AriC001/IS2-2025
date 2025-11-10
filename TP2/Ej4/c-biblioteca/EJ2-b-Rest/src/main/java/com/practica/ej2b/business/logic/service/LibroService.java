package com.practica.ej2b.business.logic.service;

import org.springframework.stereotype.Service;

import com.practica.ej2b.business.domain.entity.Libro;
import com.practica.ej2b.business.persistence.repository.LibroRepository;

@Service
public class LibroService extends BaseService<Libro, Long> {

  public LibroService(LibroRepository repository) {
    super(repository);
  }

  @Override
  protected void validar(Libro entity) throws Exception {
    if (entity.getTitulo() == null || entity.getTitulo().isEmpty()) {
      throw new Exception("El título no puede ser nulo o vacío");
    }
    if (entity.getAutores() == null || entity.getAutores().isEmpty()) {
      throw new Exception("El autor no puede ser nulo o vacío");
    }
    if (entity.getGenero() == null || entity.getGenero().isEmpty()) {
      throw new Exception("El género no puede ser nulo o vacío");
    }
    if (entity.getPaginas() <= 0) {
      throw new Exception("La cantidad de páginas debe ser mayor a cero");
    }
    if (entity.getFecha() == null) {
      throw new Exception("La fecha no puede ser nula");
    }
    
    
  }

}
