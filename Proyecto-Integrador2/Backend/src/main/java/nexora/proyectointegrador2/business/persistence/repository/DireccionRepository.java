package nexora.proyectointegrador2.business.persistence.repository;

import java.util.Collection;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import nexora.proyectointegrador2.business.domain.entity.Direccion;

@Repository
public interface DireccionRepository extends BaseRepository<Direccion, String> {

  @Query("SELECT DISTINCT d FROM Direccion d " +
         "LEFT JOIN FETCH d.localidad l " +
         "LEFT JOIN FETCH l.departamento dep " +
         "LEFT JOIN FETCH dep.provincia prov " +
         "LEFT JOIN FETCH prov.pais " +
         "WHERE d.eliminado = false")
  Collection<Direccion> findAllActivesWithLocalidad();

  @Query("SELECT DISTINCT d FROM Direccion d " +
         "LEFT JOIN FETCH d.localidad l " +
         "LEFT JOIN FETCH l.departamento dep " +
         "LEFT JOIN FETCH dep.provincia prov " +
         "LEFT JOIN FETCH prov.pais " +
         "WHERE d.id = :id AND d.eliminado = false")
  Optional<Direccion> findByIdWithLocalidad(String id);

}
