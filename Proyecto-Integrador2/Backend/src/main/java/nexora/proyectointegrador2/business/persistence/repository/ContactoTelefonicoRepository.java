package nexora.proyectointegrador2.business.persistence.repository;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import nexora.proyectointegrador2.business.domain.entity.ContactoTelefonico;

@Repository
public interface ContactoTelefonicoRepository extends BaseRepository<ContactoTelefonico, String> {

  Optional<ContactoTelefonico> findByTelefonoAndEliminadoFalse(String telefono);

  boolean existsByTelefonoAndEliminadoFalse(String telefono);

}
