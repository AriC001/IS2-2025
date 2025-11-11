package nexora.proyectointegrador2.business.logic.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import nexora.proyectointegrador2.business.domain.entity.CaracteristicaVehiculo;
import nexora.proyectointegrador2.business.domain.entity.Vehiculo;
import nexora.proyectointegrador2.business.persistence.repository.VehiculoRepository;
import org.springframework.transaction.annotation.Transactional;
import nexora.proyectointegrador2.business.persistence.repository.AlquilerRepository;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class VehiculoService extends BaseService<Vehiculo, String> {

  private final VehiculoRepository vehiculoRepository;

  @Autowired
  private CaracteristicaVehiculoService caracteristicaVehiculoService;

  private final AlquilerRepository alquilerRepository;

  public VehiculoService(VehiculoRepository repository,AlquilerRepository alquilerRepository) {
    super(repository);
    this.vehiculoRepository = repository;
    this.alquilerRepository = alquilerRepository;
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
  public Collection<Vehiculo> findAllActivesDate(Date fechaDesde, Date fechaHasta) throws Exception {
    if (fechaDesde == null) {
      fechaDesde = new Date();
    }

    // Todos los vehículos activos
    Collection<Vehiculo> vehiculos = this.findAllActives();

    // Obtener alquileres activos y determinar cuáles ocupan los vehículos en el periodo
    Collection<nexora.proyectointegrador2.business.domain.entity.Alquiler> alquileres = alquilerRepository
        .findAllByEliminadoFalse();

    Set<String> ocupados = new HashSet<>();

    for (nexora.proyectointegrador2.business.domain.entity.Alquiler a : alquileres) {
      Date aDesde = a.getFechaDesde();
      Date aHasta = a.getFechaHasta();

      boolean overlap = false;
      if (fechaHasta == null) {
        // Consulta por fecha única: fechaDesde
        Date d = fechaDesde;
        if (aDesde != null && !aDesde.after(d) && (aHasta == null || !aHasta.before(d))) {
          overlap = true;
        }
      } else {
        // Consulta por rango [fechaDesde, fechaHasta]
        Date qDesde = fechaDesde;
        Date qHasta = fechaHasta;
        // No se solapan si aHasta < qDesde OR aDesde > qHasta
        boolean noSolapan = (aHasta != null && aHasta.before(qDesde)) || (aDesde != null && aDesde.after(qHasta));
        overlap = !noSolapan;
      }

      if (overlap && a.getVehiculo() != null && a.getVehiculo().getId() != null) {
        ocupados.add(a.getVehiculo().getId());
      }
    }

    // Filtrar los vehículos que no estén en el set ocupados
    return vehiculos.stream().filter(v -> v != null && v.getId() != null && !ocupados.contains(v.getId()))
        .collect(Collectors.toList());
  }



}
