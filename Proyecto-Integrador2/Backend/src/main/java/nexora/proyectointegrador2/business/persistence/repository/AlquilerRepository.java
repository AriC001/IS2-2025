package nexora.proyectointegrador2.business.persistence.repository;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import nexora.proyectointegrador2.business.domain.entity.Alquiler;

@Repository
public interface AlquilerRepository extends BaseRepository<Alquiler, String> {

  /**
   * Obtiene todos los alquileres activos con sus relaciones cargadas (cliente, vehiculo, documento).
   * Usa JOIN FETCH para evitar problemas de lazy loading.
   */
  @Query("SELECT DISTINCT a FROM Alquiler a " +
         "LEFT JOIN FETCH a.cliente c " +
         "LEFT JOIN FETCH a.vehiculo v " +
         "LEFT JOIN FETCH v.caracteristicaVehiculo cv " +
         "LEFT JOIN FETCH a.documento d " +
         "WHERE a.eliminado = false")
  Collection<Alquiler> findAllActivesWithRelations();

}

