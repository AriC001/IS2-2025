package nexora.proyectointegrador2.business.persistence.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import java.util.Collection;
import org.springframework.stereotype.Repository;

import nexora.proyectointegrador2.business.domain.entity.Vehiculo;

@Repository
public interface VehiculoRepository extends BaseRepository<Vehiculo, String>, JpaSpecificationExecutor<Vehiculo> {

  Optional<Vehiculo> findByPatenteAndEliminadoFalse(String patente);

  boolean existsByPatenteAndEliminadoFalse(String patente);

  // La búsqueda filtrada se implementa mediante Specifications en el service.
  // Si más adelante necesitás una query estática, podés agregarla aquí.

  @Query("SELECT v.caracteristicaVehiculo.id, COUNT(v) FROM Vehiculo v WHERE v.eliminado = false GROUP BY v.caracteristicaVehiculo.id")
  List<Object[]> countVehiclesGroupByCaracteristica();

  @Query("SELECT v FROM Vehiculo v WHERE v.caracteristicaVehiculo.id IN :ids AND v.eliminado = false")
  Collection<Vehiculo> findByCaracteristicaIds(Collection<String> ids);

}
