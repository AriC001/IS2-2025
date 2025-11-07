package com.practica.nexora.ej6_e.business.persistence.repository;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.practica.nexora.ej6_e.business.domain.entity.Usuario;

@Repository
public interface UsuarioRepository extends BaseRepository<Usuario, Long> {

  Optional<Usuario> findByNombreUsuarioAndEliminadoFalse(String nombreUsuario);

}
