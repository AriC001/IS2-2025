package nexora.proyectointegrador2.business.persistence.repository;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import nexora.proyectointegrador2.business.domain.entity.Cliente;

@Repository
public interface ClienteRepository extends BaseRepository<Cliente, String> {

  Optional<Cliente> findByNumeroDocumentoAndEliminadoFalse(String numeroDocumento);

  boolean existsByNumeroDocumentoAndEliminadoFalse(String numeroDocumento);

  Optional<Cliente> findByUsuarioIdAndEliminadoFalse(String usuarioId);

}
