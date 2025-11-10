package com.practica.ej1b.business.persistence.repository;

import com.practica.ej1b.business.domain.entity.Pais;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface PaisRepositorio extends JpaRepository<Pais, String> {

  Pais findPaisByNombre(String nombre);

  Pais findByNombreAndEliminadoFalse(String nombre);

  Collection<Pais> findAllByEliminadoFalse();

  Collection<Pais> findByEliminadoFalse();
}
