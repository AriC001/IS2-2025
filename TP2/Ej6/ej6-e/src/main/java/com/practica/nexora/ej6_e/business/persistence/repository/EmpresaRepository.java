package com.practica.nexora.ej6_e.business.persistence.repository;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.practica.nexora.ej6_e.business.domain.entity.Empresa;

@Repository
public interface EmpresaRepository extends BaseRepository<Empresa, Long> {

  Optional<Empresa> findByNombreAndEliminadoFalse(String nombre);

}
