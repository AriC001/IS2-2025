package com.practica.ej1b.business.persistence.repository;

import org.springframework.stereotype.Repository;

import com.practica.ej1b.business.domain.entity.Direccion;

/**
 * Repositorio para la entidad Direccion.
 * Hereda las operaciones CRUD básicas de BaseRepository.
 */
@Repository
public interface DireccionRepositorio extends BaseRepositorio<Direccion, String> {

  /**
   * Busca una dirección por calle y numeración.
   * 
   * @param calle la calle
   * @param numeracion la numeración
   * @return la dirección encontrada o null
   */
  Direccion findByCalleAndNumeracion(String calle, String numeracion);
}
