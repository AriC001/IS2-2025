package com.example.taller.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.*;

import com.example.taller.entity.Usuario;
@Repository
public interface UsuarioRepository extends BaseRepository<Usuario, String> {
    Optional<Usuario> findByNombreUsuario(String nombreUsuario);
    @Query("SELECT u "
            + "  FROM Usuario u "
            + " WHERE u.nombreUsuario = :nombreUsuario "
            + "   AND u.clave = :clave"
            + "   AND u.eliminado = FALSE")
    public Usuario buscarUsuarioPorNombreYClave(@Param("nombreUsuario")String nombreUsuario, @Param("clave")String clave);

}