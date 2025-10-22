package com.practica.ej1b.business.persistence.repository;

import org.springframework.stereotype.Repository;

import com.practica.ej1b.business.domain.entity.Proveedor;

/**
 * Repositorio para la entidad Proveedor.
 * Hereda las operaciones CRUD básicas de BaseRepository.
 */
@Repository
public interface ProveedorRepositorio extends BaseRepositorio<Proveedor, String> {

  /**
   * Busca un proveedor por nombre y apellido.
   * 
   * @param nombre el nombre del proveedor
   * @param apellido el apellido del proveedor
   * @return el proveedor encontrado o null
   */
  Proveedor findByNombreAndApellido(String nombre, String apellido);

  /**
   * Busca un proveedor por su CUIT.
   * 
   * @param cuit el CUIT del proveedor
   * @return el proveedor encontrado o null
   */
  Proveedor findByCuit(String cuit);
}
