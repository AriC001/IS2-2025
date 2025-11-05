package nexora.proyectointegrador2.business.persistence.repository;

import java.util.Collection;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import nexora.proyectointegrador2.business.domain.entity.Provincia;

@Repository
public interface ProvinciaRepository extends BaseRepository<Provincia, String> {

  Optional<Provincia> findByNombreAndEliminadoFalse(String nombre);

  Collection<Provincia> findAllByPais_IdAndEliminadoFalse(String paisId);

}
