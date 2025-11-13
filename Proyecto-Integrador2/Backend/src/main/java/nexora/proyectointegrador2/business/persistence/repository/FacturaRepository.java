package nexora.proyectointegrador2.business.persistence.repository;

import java.util.Date;
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

  /**
   * Obtiene facturas pagadas agrupadas por modelo de vehículo con total recaudado.
   * Retorna un array de objetos: [0]=modeloVehiculo (String), [1]=marcaVehiculo (String), 
   * [2]=totalRecaudado (Double), [3]=cantidadAlquileres (Long)
   */
  @Query("SELECT cv.modelo, cv.marca, SUM(f.totalPagado), COUNT(DISTINCT df.alquiler.id) " +
         "FROM Factura f " +
         "JOIN f.detalles df " +
         "JOIN df.alquiler a " +
         "JOIN a.vehiculo v " +
         "JOIN v.caracteristicaVehiculo cv " +
         "WHERE f.eliminado = false AND f.estado = 'PAGADA' " +
         "AND f.fechaFactura >= :fechaDesde AND f.fechaFactura <= :fechaHasta " +
         "GROUP BY cv.modelo, cv.marca " +
         "ORDER BY SUM(f.totalPagado) DESC")
  java.util.Collection<Object[]> findFacturasPagadasAgrupadasPorModelo(Date fechaDesde, Date fechaHasta);
}
