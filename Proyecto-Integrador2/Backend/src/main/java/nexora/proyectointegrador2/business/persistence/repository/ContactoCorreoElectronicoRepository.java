package nexora.proyectointegrador2.business.persistence.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import nexora.proyectointegrador2.business.domain.entity.ContactoCorreoElectronico;

@Repository
public interface ContactoCorreoElectronicoRepository extends BaseRepository<ContactoCorreoElectronico, String> {

  Optional<ContactoCorreoElectronico> findByEmailAndEliminadoFalse(String email);

  boolean existsByEmailAndEliminadoFalse(String email);

  @Query("SELECT c FROM ContactoCorreoElectronico c " +
         "WHERE c.persona.id = :personaId AND c.eliminado = false")
  Optional<ContactoCorreoElectronico> findByPersonaIdAndEliminadoFalse(String personaId);

}
