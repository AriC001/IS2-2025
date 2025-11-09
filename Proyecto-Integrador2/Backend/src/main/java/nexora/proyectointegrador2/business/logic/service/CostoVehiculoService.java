package nexora.proyectointegrador2.business.logic.service;

import org.springframework.stereotype.Service;

import nexora.proyectointegrador2.business.domain.entity.CostoVehiculo;
import nexora.proyectointegrador2.business.persistence.repository.CostoVehiculoRepository;

@Service
public class CostoVehiculoService extends BaseService<CostoVehiculo, String> {

  public CostoVehiculoService(CostoVehiculoRepository repository) {
    super(repository);
  }

  @Override
  protected void validar(CostoVehiculo entity) throws Exception {
    if (entity.getFechaDesde() == null) {
      throw new Exception("La fecha desde es obligatoria");
    }
    if (entity.getFechaHasta() == null) {
      throw new Exception("La fecha hasta es obligatoria");
    }
    if (entity.getFechaDesde().after(entity.getFechaHasta())) {
      throw new Exception("La fecha desde no puede ser posterior a la fecha hasta");
    }
    if (entity.getCosto() == null || entity.getCosto() <= 0) {
      throw new Exception("El costo debe ser mayor a 0");
    }
  }

}
