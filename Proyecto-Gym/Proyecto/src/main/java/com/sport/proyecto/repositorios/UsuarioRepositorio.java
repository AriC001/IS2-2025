package com.sport.proyecto.repositorios;

import com.sport.proyecto.entidades.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepositorio extends JpaRepository<Usuario, Long> {

  @Query(value = "SELECT * FROM usuario WHERE usuario.nombre_usuario =:nombreUsuario AND usuario.clave =:clave AND usuario.eliminado = false", nativeQuery = true)
  public Optional<Usuario> buscarPorNombreUsuarioYClave(@Param("nombreUsuario") String nombreUsuario, @Param("clave") String clave);
}
