package nexora.proyectointegrador2.business.logic.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import nexora.proyectointegrador2.business.domain.entity.CaracteristicaVehiculo;
import nexora.proyectointegrador2.business.domain.entity.Vehiculo;
import nexora.proyectointegrador2.business.persistence.repository.VehiculoRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;
// no unused imports
import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Subquery;
import jakarta.persistence.criteria.Root;

@Service
public class VehiculoService extends BaseService<Vehiculo, String> {

  private final VehiculoRepository vehiculoRepository;

  @Autowired
  private CaracteristicaVehiculoService caracteristicaVehiculoService;

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

    Specification<Vehiculo> spec = (root, query, cb) -> {
      List<Predicate> preds = new ArrayList<>();
      // Solo activos
      preds.add(cb.isFalse(root.get("eliminado")));

      if (marca != null) {
        preds.add(cb.like(cb.lower(root.get("marca")), "%" + marca.toLowerCase() + "%"));
      }
      if (modelo != null) {
        preds.add(cb.like(cb.lower(root.get("modelo")), "%" + modelo.toLowerCase() + "%"));
      }
      if (anio != null) {
        preds.add(cb.equal(root.get("anio"), anio));
      }

      // Subquery que obtiene caracteristica_vehiculo_id que están completamente ocupadas
      // Es decir, aquellas caracteristicas cuyo número de alquileres que se solapan >= cantidadVehiculoDisponible
      Subquery<String> fullBooked = query.subquery(String.class);
      Root<nexora.proyectointegrador2.business.domain.entity.Alquiler> aRoot = fullBooked
          .from(nexora.proyectointegrador2.business.domain.entity.Alquiler.class);
      // JOIN a.vehiculo -> vehiculo.caracteristicaVehiculo
      jakarta.persistence.criteria.Join<?, ?> vehJoin = aRoot.join("vehiculo");
      jakarta.persistence.criteria.Join<?, ?> carJoin = vehJoin.join("caracteristicaVehiculo");

      Predicate activoAlq = cb.isFalse(aRoot.get("eliminado"));

      Predicate overlapAlq;
      if (qHasta == null) {
        overlapAlq = cb.and(cb.lessThanOrEqualTo(aRoot.get("fechaDesde"), qDesde),
            cb.or(cb.isNull(aRoot.get("fechaHasta")), cb.greaterThanOrEqualTo(aRoot.get("fechaHasta"), qDesde)));
      } else {
        Predicate aHastaBeforeQDesde = cb.and(cb.isNotNull(aRoot.get("fechaHasta")), cb.lessThan(aRoot.get("fechaHasta"), qDesde));
        Predicate aDesdeAfterQHasta = cb.greaterThan(aRoot.get("fechaDesde"), qHasta);
        overlapAlq = cb.not(cb.or(aHastaBeforeQDesde, aDesdeAfterQHasta));
      }

      // Seleccionamos id de caracteristica que están totalmente ocupadas
      fullBooked.select(carJoin.get("id"))
          .where(cb.and(activoAlq, overlapAlq))
          .groupBy(carJoin.get("id"), carJoin.get("cantidadVehiculoDisponible"))
          .having(cb.greaterThanOrEqualTo(cb.count(aRoot), carJoin.get("cantidadVehiculoDisponible").as(Long.class)));

      // Excluir vehículos cuya caracteristica está en la lista de fully booked
      preds.add(cb.not(root.get("caracteristicaVehiculo").get("id").in(fullBooked)));

      return cb.and(preds.toArray(new Predicate[0]));
    };

    return vehiculoRepository.findAll(spec);
  }



}
