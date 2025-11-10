package nexora.proyectointegrador2.business.logic.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import nexora.proyectointegrador2.business.domain.entity.CaracteristicaVehiculo;
import nexora.proyectointegrador2.business.domain.entity.Vehiculo;
import nexora.proyectointegrador2.business.persistence.repository.VehiculoRepository;

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

}
