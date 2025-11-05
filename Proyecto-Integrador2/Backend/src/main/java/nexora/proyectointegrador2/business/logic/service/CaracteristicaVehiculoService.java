package nexora.proyectointegrador2.business.logic.service;

import org.springframework.stereotype.Service;

import nexora.proyectointegrador2.business.domain.entity.CaracteristicaVehiculo;
import nexora.proyectointegrador2.business.persistence.repository.CaracteristicaVehiculoRepository;

@Service
public class CaracteristicaVehiculoService extends BaseService<CaracteristicaVehiculo, String> {

  public CaracteristicaVehiculoService(CaracteristicaVehiculoRepository repository) {
    super(repository);
  }

  @Override
  protected void validar(CaracteristicaVehiculo entity) throws Exception {
    if (entity.getMarca() == null || entity.getMarca().trim().isEmpty()) {
      throw new Exception("La marca es obligatoria");
    }
    if (entity.getModelo() == null || entity.getModelo().trim().isEmpty()) {
      throw new Exception("El modelo es obligatorio");
    }
    if (entity.getAnio() == null || entity.getAnio() < 1900 || entity.getAnio() > 2100) {
      throw new Exception("El año debe estar entre 1900 y 2100");
    }
    if (entity.getCantidadPuerta() == null || entity.getCantidadPuerta() <= 0) {
      throw new Exception("La cantidad de puertas debe ser mayor a 0");
    }
    if (entity.getCantidadAsiento() == null || entity.getCantidadAsiento() <= 0) {
      throw new Exception("La cantidad de asientos debe ser mayor a 0");
    }
    if (entity.getCostoVehiculo() == null) {
      throw new Exception("El costo del vehículo es obligatorio");
    }
  }

}
