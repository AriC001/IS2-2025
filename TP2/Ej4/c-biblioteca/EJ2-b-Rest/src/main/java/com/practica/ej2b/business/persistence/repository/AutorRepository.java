package com.practica.ej2b.business.persistence.repository;

import org.springframework.stereotype.Repository;

import com.practica.ej2b.business.domain.entity.Autor;

@Repository
public interface AutorRepository extends BaseRepository<Autor, Long> {

}
