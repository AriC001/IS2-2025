package nexora.proyectointegrador2.business.persistence.repository;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import nexora.proyectointegrador2.business.domain.entity.Usuario;

@Repository
public interface UsuarioRepository extends BaseRepository<Usuario, String> {

  Optional<Usuario> findByNombreUsuario(String nombreUsuario);

  Optional<Usuario> findByNombreUsuarioAndEliminadoFalse(String nombreUsuario);

  boolean existsByNombreUsuarioAndEliminadoFalse(String nombreUsuario);

}
