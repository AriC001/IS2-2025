package com.practica.ej1b.business.persistence.repository;


import java.util.Collection;

import org.springframework.stereotype.Repository;

import com.practica.ej1b.business.domain.dto.Item;
import com.practica.ej1b.business.domain.entity.Pais;

@Repository
public interface PaisRepositorio extends BaseRepositorio<Pais, String> {

  Pais findByNombreAndEliminadoFalse(String string);

  Collection<Pais> findAllByEliminadoFalse();

}