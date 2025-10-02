package com.practica.ej1b.business.persistence.repository;

import com.practica.ej1b.business.domain.entity.Departamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DepartamentoRepositorio extends JpaRepository<Departamento, String> {

  List<Departamento> findAllByEliminadoFalse();

  Departamento findByNombre(String nombre);

  List<Departamento> findByProvinciaIdAndEliminadoFalse(String provinciaId);

}
