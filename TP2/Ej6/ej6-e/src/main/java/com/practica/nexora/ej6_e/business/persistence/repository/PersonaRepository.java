package com.practica.nexora.ej6_e.business.persistence.repository;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.practica.nexora.ej6_e.business.domain.entity.Persona;

@Repository
public interface PersonaRepository extends BaseRepository<Persona, Long> {

  Optional<Persona> findByNombreAndEliminadoFalse(String nombre);

}
