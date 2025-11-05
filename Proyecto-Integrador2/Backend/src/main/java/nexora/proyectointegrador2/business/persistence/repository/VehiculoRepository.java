package nexora.proyectointegrador2.business.persistence.repository;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import nexora.proyectointegrador2.business.domain.entity.Vehiculo;

@Repository
public interface VehiculoRepository extends BaseRepository<Vehiculo, String> {

  Optional<Vehiculo> findByPatenteAndEliminadoFalse(String patente);

  boolean existsByPatenteAndEliminadoFalse(String patente);

}
