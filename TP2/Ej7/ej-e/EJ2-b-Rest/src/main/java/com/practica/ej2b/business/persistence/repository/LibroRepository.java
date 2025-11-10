package com.practica.ej2b.business.persistence.repository;

import org.springframework.stereotype.Repository;

import com.practica.ej2b.business.domain.entity.Libro;

@Repository
public interface LibroRepository extends BaseRepository<Libro, Long> {

}
