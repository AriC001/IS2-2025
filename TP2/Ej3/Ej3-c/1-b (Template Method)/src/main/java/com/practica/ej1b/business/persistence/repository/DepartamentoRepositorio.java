package com.practica.ej1b.business.persistence.repository;

import java.util.Collection;

import org.springframework.stereotype.Repository;

import com.practica.ej1b.business.domain.entity.Departamento;

/**
 * Repositorio para la entidad Departamento.
 * Hereda las operaciones CRUD básicas de BaseRepository.
 */
@Repository
public interface DepartamentoRepositorio extends BaseRepositorio<Departamento, String> {

  /**
   * Busca un departamento por su nombre.
   * 
   * @param nombre el nombre del departamento
   * @return el departamento encontrado o null
   */
  Departamento findByNombre(String nombre);

  /**
   * Busca todos los departamentos de una provincia específica que no estén eliminados.
   * 
   * @param provinciaId el ID de la provincia
   * @return colección de departamentos activos de la provincia
   */
  Collection<Departamento> findByProvinciaIdAndEliminadoFalse(String provinciaId);
}
