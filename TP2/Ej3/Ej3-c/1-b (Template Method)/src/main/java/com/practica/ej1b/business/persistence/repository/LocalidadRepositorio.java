package com.practica.ej1b.business.persistence.repository;

import java.util.Collection;

import org.springframework.stereotype.Repository;

import com.practica.ej1b.business.domain.entity.Localidad;

/**
 * Repositorio para la entidad Localidad.
 * Hereda las operaciones CRUD básicas de BaseRepository.
 */
@Repository
public interface LocalidadRepositorio extends BaseRepositorio<Localidad, String> {

  /**
   * Busca una localidad por su nombre.
   * 
   * @param nombre el nombre de la localidad
   * @return la localidad encontrada o null
   */
  Localidad findLocalidadByNombre(String nombre);

  /**
   * Busca todas las localidades de un departamento específico que no estén eliminadas.
   * 
   * @param departamentoId el ID del departamento
   * @return colección de localidades activas del departamento
   */
  Collection<Localidad> findByDepartamentoIdAndEliminadoFalse(String departamentoId);
}
