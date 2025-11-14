package nexora.proyectointegrador2.business.logic.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import nexora.proyectointegrador2.business.domain.entity.CaracteristicaVehiculo;
import nexora.proyectointegrador2.business.domain.entity.Vehiculo;
import nexora.proyectointegrador2.business.persistence.repository.AlquilerRepository;
import nexora.proyectointegrador2.business.persistence.repository.VehiculoRepository;

@Service
public class VehiculoService extends BaseService<Vehiculo, String> {

  private final VehiculoRepository vehiculoRepository;

  @Autowired
  private CaracteristicaVehiculoService caracteristicaVehiculoService;

  @Autowired
  private AlquilerRepository alquilerRepository;

  public VehiculoService(VehiculoRepository repository) {
    super(repository);
    this.vehiculoRepository = repository;
  }

  @Override
  protected void validar(Vehiculo entity) throws Exception {
    if (entity.getPatente() == null || entity.getPatente().trim().isEmpty()) {
      throw new Exception("La patente es obligatoria");
    }
    if (entity.getEstadoVehiculo() == null) {
      throw new Exception("El estado del vehículo es obligatorio");
    }
    if (entity.getCaracteristicaVehiculo() == null) {
      throw new Exception("La característica del vehículo es obligatoria");
    }
    
    // Validar que la patente sea única
    if (vehiculoRepository.findByPatenteAndEliminadoFalse(entity.getPatente()).isPresent() 
        && (entity.getId() == null || !vehiculoRepository.findByPatenteAndEliminadoFalse(entity.getPatente()).get().getId().equals(entity.getId()))) {
      throw new Exception("Ya existe un vehículo con la patente: " + entity.getPatente());
    }
  }

  @Override
  protected void preAlta(Vehiculo entity) throws Exception {
    if (entity.getCaracteristicaVehiculo() != null && entity.getCaracteristicaVehiculo().getId() == null) {
      CaracteristicaVehiculo caracteristicaGuardada = caracteristicaVehiculoService.save(entity.getCaracteristicaVehiculo());
      entity.setCaracteristicaVehiculo(caracteristicaGuardada);
    } else if (entity.getCaracteristicaVehiculo() != null && entity.getCaracteristicaVehiculo().getId() != null) {
      CaracteristicaVehiculo caracteristicaExistente = caracteristicaVehiculoService.findById(entity.getCaracteristicaVehiculo().getId());
      entity.setCaracteristicaVehiculo(caracteristicaExistente);
    }
  }

  @Override
  protected void preUpdate(String id, Vehiculo entity) throws Exception {
    if (entity.getCaracteristicaVehiculo() != null && entity.getCaracteristicaVehiculo().getId() == null) {
      CaracteristicaVehiculo caracteristicaGuardada = caracteristicaVehiculoService.save(entity.getCaracteristicaVehiculo());
      entity.setCaracteristicaVehiculo(caracteristicaGuardada);
    } else if (entity.getCaracteristicaVehiculo() != null && entity.getCaracteristicaVehiculo().getId() != null) {
      Vehiculo vehiculoExistente = findById(id);
      if (vehiculoExistente.getCaracteristicaVehiculo() == null ||
          !vehiculoExistente.getCaracteristicaVehiculo().getId().equals(entity.getCaracteristicaVehiculo().getId())) {
        CaracteristicaVehiculo caracteristicaExistente = caracteristicaVehiculoService.findById(entity.getCaracteristicaVehiculo().getId());
        entity.setCaracteristicaVehiculo(caracteristicaExistente);
      }
    }
  }

  /**
   * Devuelve los vehículos activos que NO estén alquilados en la fecha o rango dado.
   * Si fechaHasta es null se interpreta como consulta por la fecha única fechaDesde.
   * Si fechaDesde es null se considerará la fecha actual (aunque el controller ya lo asigna).
   */
  @Transactional(readOnly = true)
  public Collection<Vehiculo> findAllActivesFilter(Date fechaDesde, Date fechaHasta, String marcaS, String modeloS,
      Integer anioS) throws Exception {
    // Normalizar filtros vacíos
    final String marca = (marcaS == null || marcaS.trim().isEmpty()) ? null : marcaS.trim();
    final String modelo = (modeloS == null || modeloS.trim().isEmpty()) ? null : modeloS.trim();
    final Integer anio = (anioS == null || anioS == 0) ? null : anioS;

    final Date qDesde = (fechaDesde == null) ? new Date() : fechaDesde;
    final Date qHasta = fechaHasta; // may be null

    // Primero: obtener conteo total de vehículos por caracteristica (desde repo)
    List<Object[]> totals = vehiculoRepository.countVehiclesGroupByCaracteristica();
    java.util.Map<String, Long> totalByChar = new java.util.HashMap<>();
    for (Object[] row : totals) {
      if (row == null || row.length < 2) continue;
      String charId = (String) row[0];
      Long cnt = ((Number) row[1]).longValue();
      totalByChar.put(charId, cnt);
    }

    // Segundo: obtener conteo de alquileres solapados por caracteristica según la ventana
    Collection<Object[]> overlaps = (qHasta == null)
        ? alquilerRepository.countOverlappingByCaracteristicaForDate(qDesde)
        : alquilerRepository.countOverlappingByCaracteristicaForRange(qDesde, qHasta);
    java.util.Set<String> fullyBookedIds = new java.util.HashSet<>();
    for (Object[] row : overlaps) {
      if (row == null || row.length < 2) continue;
      String charId = (String) row[0];
      Long alquCount = ((Number) row[1]).longValue();
      Long total = totalByChar.getOrDefault(charId, 0L);
      if (alquCount >= total && total > 0) {
        fullyBookedIds.add(charId);
      }
    }

    // Finalmente build Specification simple: filtros por marca/modelo/anio y excluir caracteristicas fully booked
    Specification<Vehiculo> spec2 = (root, query, cb) -> {
      List<Predicate> preds = new ArrayList<>();
      Join<Object, Object> caracteristicaJoin = root.join("caracteristicaVehiculo");
      preds.add(cb.isFalse(root.get("eliminado")));
      if (marca != null) preds.add(cb.like(cb.lower(caracteristicaJoin.get("marca")), "%" + marca.toLowerCase() + "%"));
      if (modelo != null) preds.add(cb.like(cb.lower(caracteristicaJoin.get("modelo")), "%" + modelo.toLowerCase() + "%"));
      if (anio != null) preds.add(cb.equal(caracteristicaJoin.get("anio"), anio));
      if (!fullyBookedIds.isEmpty()) preds.add(cb.not(caracteristicaJoin.get("id").in(fullyBookedIds)));
      return cb.and(preds.toArray(new Predicate[0]));
    };

    return vehiculoRepository.findAll(spec2);
  }


  @Transactional(readOnly = true)
  public boolean checkAvailability(Date fechaDesde, Date fechaHasta, String idVehiculo) throws Exception {
    if (idVehiculo == null || idVehiculo.trim().isEmpty()) {
      throw new IllegalArgumentException("idVehiculo es requerido");
    }
    if (fechaDesde == null) {
      throw new IllegalArgumentException("fechaDesde es requerida");
    }
    if (fechaHasta == null) {
      throw new IllegalArgumentException("fechaHasta es requerida");
    }
    if (fechaDesde.after(fechaHasta)) {
      throw new IllegalArgumentException("fechaDesde no puede ser posterior a fechaHasta");
    }

    final java.util.Date qDesde = fechaDesde;
    final java.util.Date qHasta = fechaHasta;

    Long overlaps = alquilerRepository.countOverlappingForVehicleRange(qDesde, qHasta, idVehiculo);
    
    if (overlaps == null) {
      overlaps = 0L;
    }
    
    boolean isAvailable = overlaps == 0L;
    return isAvailable;
  }


}
