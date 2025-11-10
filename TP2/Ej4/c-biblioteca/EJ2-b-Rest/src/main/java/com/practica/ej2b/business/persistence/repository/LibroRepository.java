package com.practica.ej2b.business.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.practica.ej2b.business.domain.entity.Libro;

@Repository
public interface LibroRepository extends BaseRepository<Libro, Long> {

  List<Libro> findAllByGenero(String genero);

  List<Libro> findAllByTitulo(String titulo);

  @Query("SELECT l FROM Libro l JOIN l.autores a WHERE a.nombre = :nombre AND a.apellido = :apellido")
  List<Libro> findAllByAutor(@Param("nombre") String nombre, @Param("apellido") String apellido);

}
