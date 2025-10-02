package com.practica.ej1b.business.persistence.repository;

import com.practica.ej1b.business.domain.entity.Localidad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface LocalidadRepositorio extends JpaRepository<Localidad, String> {

  Localidad findLocalidadByNombre(String nombre);

  Collection<Localidad> findAllByEliminadoFalse();

  Collection<Localidad> findByDepartamentoIdAndEliminadoFalse(String departamentoId);

}
