package com.practica.ej1b.business.persistence.repository;

import com.practica.ej1b.business.domain.entity.Provincia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface ProvinciaRepositorio extends JpaRepository<Provincia, String> {

  Collection<Provincia> findAllByEliminadoFalse();

  Provincia findProvinciaByNombre(String nombre);

  Collection<Provincia> findByPaisIdAndEliminadoFalse(String paisId);

}
