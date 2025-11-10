package nexora.proyectointegrador2.business.persistence.repository;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import nexora.proyectointegrador2.business.domain.entity.ContactoCorreoElectronico;

@Repository
public interface ContactoCorreoElectronicoRepository extends BaseRepository<ContactoCorreoElectronico, String> {

  Optional<ContactoCorreoElectronico> findByEmailAndEliminadoFalse(String email);

  boolean existsByEmailAndEliminadoFalse(String email);

}
