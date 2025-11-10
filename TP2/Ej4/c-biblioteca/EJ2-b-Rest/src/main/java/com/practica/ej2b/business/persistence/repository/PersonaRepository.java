package com.practica.ej2b.business.persistence.repository;

import org.springframework.stereotype.Repository;

import com.practica.ej2b.business.domain.entity.Persona;


@Repository
public interface PersonaRepository extends BaseRepository<Persona, Long> {

}
