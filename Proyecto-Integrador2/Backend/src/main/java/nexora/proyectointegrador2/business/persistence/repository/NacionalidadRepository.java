package nexora.proyectointegrador2.business.persistence.repository;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import nexora.proyectointegrador2.business.domain.entity.Nacionalidad;

@Repository
public interface NacionalidadRepository extends BaseRepository<Nacionalidad, String> {

  Optional<Nacionalidad> findByNombreAndEliminadoFalse(String nombre);

}
