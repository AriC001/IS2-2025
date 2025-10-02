package com.practica.ej1b.business.persistence.repository;

import com.practica.ej1b.business.domain.entity.Direccion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface DireccionRepositorio extends JpaRepository<Direccion, String> {

  Collection<Direccion> findAllByEliminadoFalse();

  Direccion findByCalleAndNumeracion(String calle, String numeracion);


}
