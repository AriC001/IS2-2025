package nexora.proyectointegrador2.business.persistence.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import nexora.proyectointegrador2.business.domain.entity.Factura;

@Repository
public interface FacturaRepository extends BaseRepository<Factura, String> {

  Optional<Factura> findByNumeroFactura(Long numeroFactura);

  boolean existsByNumeroFactura(Long numeroFactura);

  @Query("SELECT MAX(f.numeroFactura) FROM Factura f WHERE f.eliminado = false")
  Long findMaxNumeroFactura();

  @Query("SELECT f FROM Factura f " +
         "JOIN f.detalles d " +
         "WHERE d.alquiler.id = :alquilerId AND f.eliminado = false")
  Optional<Factura> findByAlquilerId(String alquilerId);
}
