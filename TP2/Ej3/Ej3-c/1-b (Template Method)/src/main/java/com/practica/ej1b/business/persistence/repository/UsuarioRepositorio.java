package com.practica.ej1b.business.persistence.repository;

import org.springframework.stereotype.Repository;

import com.practica.ej1b.business.domain.entity.Usuario;

/**
 * Repositorio para la entidad Usuario.
 * Hereda las operaciones CRUD básicas de BaseRepository.
 */
@Repository
public interface UsuarioRepositorio extends BaseRepositorio<Usuario, String> {

  /**
   * Busca un usuario por su nombre de usuario que no esté eliminado.
   * 
   * @param nombreUsuario el nombre de usuario
   * @return el usuario activo encontrado o null
   */
  Usuario findUsuarioByNombreUsuarioAndEliminadoFalse(String nombreUsuario);
}
