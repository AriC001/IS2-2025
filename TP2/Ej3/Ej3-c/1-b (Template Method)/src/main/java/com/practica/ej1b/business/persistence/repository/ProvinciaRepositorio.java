package com.practica.ej1b.business.persistence.repository;


import com.practica.ej1b.business.domain.dto.Item;
import com.practica.ej1b.business.domain.entity.Provincia;

import java.util.Collection;

import org.springframework.stereotype.Repository;


@Repository
public interface ProvinciaRepositorio extends BaseRepositorio<Provincia, String> {

  Collection<Provincia> findByPaisIdAndEliminadoFalse(String paisId);


}
