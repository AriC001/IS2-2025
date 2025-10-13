package com.practica.ej1b.business.persistence.repository;

import com.practica.ej1b.business.domain.entity.Direccion;
import com.practica.ej1b.business.domain.entity.Empresa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface EmpresaRepositorio extends JpaRepository<Empresa, String> {

  Collection<Empresa> findAllByEliminadoFalse();

  Empresa findByRazonSocial(String razonSocial);
}
