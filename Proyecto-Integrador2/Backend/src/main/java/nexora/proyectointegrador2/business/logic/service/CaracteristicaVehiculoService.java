package nexora.proyectointegrador2.business.logic.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import nexora.proyectointegrador2.business.domain.entity.CaracteristicaVehiculo;
import nexora.proyectointegrador2.business.domain.entity.CostoVehiculo;
import nexora.proyectointegrador2.business.domain.entity.Imagen;
import nexora.proyectointegrador2.business.persistence.repository.CaracteristicaVehiculoRepository;

@Service
public class CaracteristicaVehiculoService extends BaseService<CaracteristicaVehiculo, String> {

  @Autowired
  private ImagenService imagenService;

  @Autowired
  private CostoVehiculoService costoVehiculoService;

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

  @Override
  protected void preAlta(CaracteristicaVehiculo entity) throws Exception {
    if (entity.getImagenVehiculo() != null && entity.getImagenVehiculo().getId() == null) {
      Imagen imagenGuardada = imagenService.save(entity.getImagenVehiculo());
      entity.setImagenVehiculo(imagenGuardada);
    } else if (entity.getImagenVehiculo() != null && entity.getImagenVehiculo().getId() != null) {
      Imagen imagenExistente = imagenService.findById(entity.getImagenVehiculo().getId());
      entity.setImagenVehiculo(imagenExistente);
    }

    if (entity.getCostoVehiculo() != null && entity.getCostoVehiculo().getId() == null) {
      CostoVehiculo costoGuardado = costoVehiculoService.save(entity.getCostoVehiculo());
      entity.setCostoVehiculo(costoGuardado);
    } else if (entity.getCostoVehiculo() != null && entity.getCostoVehiculo().getId() != null) {
      CostoVehiculo costoExistente = costoVehiculoService.findById(entity.getCostoVehiculo().getId());
      entity.setCostoVehiculo(costoExistente);
    }
  }

  @Override
  protected void preUpdate(String id, CaracteristicaVehiculo entity) throws Exception {
    if (entity.getImagenVehiculo() != null && entity.getImagenVehiculo().getId() == null) {
      Imagen imagenGuardada = imagenService.save(entity.getImagenVehiculo());
      entity.setImagenVehiculo(imagenGuardada);
    } else if (entity.getImagenVehiculo() != null && entity.getImagenVehiculo().getId() != null) {
      CaracteristicaVehiculo caracteristicaExistente = findById(id);
      if (caracteristicaExistente.getImagenVehiculo() == null ||
          !caracteristicaExistente.getImagenVehiculo().getId().equals(entity.getImagenVehiculo().getId())) {
        Imagen imagenExistente = imagenService.findById(entity.getImagenVehiculo().getId());
        entity.setImagenVehiculo(imagenExistente);
      }
    }

    if (entity.getCostoVehiculo() != null && entity.getCostoVehiculo().getId() == null) {
      CostoVehiculo costoGuardado = costoVehiculoService.save(entity.getCostoVehiculo());
      entity.setCostoVehiculo(costoGuardado);
    } else if (entity.getCostoVehiculo() != null && entity.getCostoVehiculo().getId() != null) {
      CaracteristicaVehiculo caracteristicaExistente = findById(id);
      if (caracteristicaExistente.getCostoVehiculo() == null ||
          !caracteristicaExistente.getCostoVehiculo().getId().equals(entity.getCostoVehiculo().getId())) {
        CostoVehiculo costoExistente = costoVehiculoService.findById(entity.getCostoVehiculo().getId());
        entity.setCostoVehiculo(costoExistente);
      }
    }
  }

}
