package nexora.proyectointegrador2.business.persistence.repository;

import java.util.Collection;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import nexora.proyectointegrador2.business.domain.entity.Localidad;

@Repository
public interface LocalidadRepository extends BaseRepository<Localidad, String> {

  Optional<Localidad> findByNombreAndEliminadoFalse(String nombre);

  Collection<Localidad> findAllByDepartamento_IdAndEliminadoFalse(String departamentoId);

}
