package nexora.proyectointegrador2.business.persistence.repository;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import nexora.proyectointegrador2.business.domain.entity.Alquiler;
import java.util.Date;

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

  @Query("SELECT v.caracteristicaVehiculo.id, COUNT(a) FROM Alquiler a JOIN a.vehiculo v "
    + "WHERE a.eliminado = false AND a.fechaDesde <= :qDesde AND (a.fechaHasta IS NULL OR a.fechaHasta >= :qDesde) "
    + "GROUP BY v.caracteristicaVehiculo.id")
  Collection<Object[]> countOverlappingByCaracteristicaForDate(java.util.Date qDesde);

  @Query("SELECT v.caracteristicaVehiculo.id, COUNT(a) FROM Alquiler a JOIN a.vehiculo v "
    + "WHERE a.eliminado = false AND NOT (a.fechaHasta IS NOT NULL AND a.fechaHasta < :qDesde OR a.fechaDesde > :qHasta) "
    + "GROUP BY v.caracteristicaVehiculo.id")
  Collection<Object[]> countOverlappingByCaracteristicaForRange(java.util.Date qDesde, java.util.Date qHasta);

  @Query("SELECT COUNT(a) FROM Alquiler a WHERE a.eliminado = false AND a.vehiculo.id = :vehiculoId "
    + "AND a.fechaDesde <= :qDesde AND (a.fechaHasta IS NULL OR a.fechaHasta >= :qDesde)")
  Long countOverlappingForVehicleDate(java.util.Date qDesde, String vehiculoId);

  @Query("SELECT COUNT(a) FROM Alquiler a WHERE a.eliminado = false AND a.vehiculo.id = :vehiculoId "
    + "AND NOT (a.fechaHasta IS NOT NULL AND a.fechaHasta < :qDesde OR a.fechaDesde > :qHasta)")
  Long countOverlappingForVehicleRange(java.util.Date qDesde, java.util.Date qHasta, String vehiculoId);

  /**
   * Obtiene un alquiler por ID con todas sus relaciones cargadas.
   */
  @Query("SELECT DISTINCT a FROM Alquiler a " +
         "LEFT JOIN FETCH a.cliente c " +
         "LEFT JOIN FETCH c.contactos " +
         "LEFT JOIN FETCH a.vehiculo v " +
         "LEFT JOIN FETCH v.caracteristicaVehiculo cv " +
         "LEFT JOIN FETCH cv.costoVehiculo " +
         "LEFT JOIN FETCH a.documento d " +
         "WHERE a.id = :id AND a.eliminado = false")
  java.util.Optional<Alquiler> findByIdWithRelations(String id);

  /**
   * Obtiene alquileres activos cuya fecha de devolución (fechaHasta) es la fecha especificada.
   * Compara solo la parte de fecha (sin hora) usando DATE().
   * Carga todas las relaciones necesarias para enviar recordatorios.
   */
  @Query("SELECT DISTINCT a FROM Alquiler a " +
         "LEFT JOIN FETCH a.cliente c " +
         "LEFT JOIN FETCH c.contactos " +
         "LEFT JOIN FETCH a.vehiculo v " +
         "LEFT JOIN FETCH v.caracteristicaVehiculo cv " +
         "WHERE a.eliminado = false " +
         "AND DATE(a.fechaHasta) = DATE(:fechaDevolucion) " +
         "AND a.estadoAlquiler = 'PAGADO'")
  Collection<Alquiler> findAlquileresConDevolucionMañana(Date fechaDevolucion);

  /**
   * Obtiene todos los alquileres activos que tienen una factura asociada.
   * Carga todas las relaciones necesarias para reportes.
   */
  @Query("SELECT DISTINCT a FROM Alquiler a " +
         "LEFT JOIN FETCH a.cliente c " +
         "LEFT JOIN FETCH a.vehiculo v " +
         "LEFT JOIN FETCH v.caracteristicaVehiculo cv " +
         "JOIN DetalleFactura df ON df.alquiler.id = a.id " +
         "JOIN Factura f ON f.id = df.factura.id " +
         "WHERE a.eliminado = false AND f.eliminado = false")
  Collection<Alquiler> findAlquileresConFactura();

  /**
   * Obtiene alquileres activos con factura asociada en un periodo determinado.
   * Filtra por fecha de factura dentro del rango especificado.
   * Carga todas las relaciones necesarias para reportes.
   */
  @Query("SELECT DISTINCT a FROM Alquiler a " +
         "LEFT JOIN FETCH a.cliente c " +
         "LEFT JOIN FETCH a.vehiculo v " +
         "LEFT JOIN FETCH v.caracteristicaVehiculo cv " +
         "JOIN DetalleFactura df ON df.alquiler.id = a.id " +
         "JOIN Factura f ON f.id = df.factura.id " +
         "WHERE a.eliminado = false AND f.eliminado = false " +
         "AND f.fechaFactura >= :fechaDesde AND f.fechaFactura <= :fechaHasta")
  Collection<Alquiler> findAlquileresConFacturaPorPeriodo(Date fechaDesde, Date fechaHasta);

  /**
   * Obtiene alquileres activos filtrados por usuarioId.
   * Busca alquileres donde el cliente asociado tiene un usuario con el ID especificado.
   * Carga todas las relaciones necesarias.
   */
  @Query("SELECT DISTINCT a FROM Alquiler a " +
         "LEFT JOIN FETCH a.cliente c " +
         "LEFT JOIN FETCH c.usuario u " +
         "LEFT JOIN FETCH a.vehiculo v " +
         "LEFT JOIN FETCH v.caracteristicaVehiculo cv " +
         "LEFT JOIN FETCH a.documento d " +
         "WHERE a.eliminado = false AND u.id = :usuarioId")
  Collection<Alquiler> findByUsuarioId(String usuarioId);

}


