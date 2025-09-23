package com.sport.proyecto.repositorios;

import com.sport.proyecto.entidades.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepositorio extends JpaRepository<Usuario, String> {

  @Query(value = "SELECT * FROM usuario WHERE usuario.eliminado = false", nativeQuery = true)
  List<Usuario> findAllActives();

  @Query(value = "SELECT * FROM usuario WHERE usuario.nombre_usuario =:nombreUsuario AND usuario.eliminado = false", nativeQuery = true)
  Optional<Usuario> findByUsername(@Param("nombreUsuario") String nombreUsuario);

}
