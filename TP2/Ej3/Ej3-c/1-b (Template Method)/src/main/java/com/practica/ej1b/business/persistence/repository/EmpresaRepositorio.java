package com.practica.ej1b.business.persistence.repository;

import org.springframework.stereotype.Repository;

import com.practica.ej1b.business.domain.entity.Empresa;

/**
 * Repositorio para la entidad Empresa.
 * Hereda las operaciones CRUD básicas de BaseRepository.
 */
@Repository
public interface EmpresaRepositorio extends BaseRepositorio<Empresa, String> {

  /**
   * Busca una empresa por su razón social.
   * 
   * @param razonSocial la razón social de la empresa
   * @return la empresa encontrada o null
   */
  Empresa findByRazonSocial(String razonSocial);
}
