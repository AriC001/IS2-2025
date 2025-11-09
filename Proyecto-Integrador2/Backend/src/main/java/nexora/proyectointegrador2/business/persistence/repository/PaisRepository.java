package nexora.proyectointegrador2.business.persistence.repository;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import nexora.proyectointegrador2.business.domain.entity.Pais;

@Repository
public interface PaisRepository extends BaseRepository<Pais, String> {

  Optional<Pais> findByNombreAndEliminadoFalse(String nombre);

}
