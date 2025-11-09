package nexora.proyectointegrador2.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import nexora.proyectointegrador2.business.domain.entity.CaracteristicaVehiculo;
import nexora.proyectointegrador2.business.logic.service.CaracteristicaVehiculoService;
import nexora.proyectointegrador2.utils.dto.CaracteristicaVehiculoDTO;
import nexora.proyectointegrador2.utils.mapper.impl.CaracteristicaVehiculoMapper;

@RestController
@RequestMapping("api/v1/caracteristicas-vehiculo")
public class CaracteristicaVehiculoRestController extends BaseRestController<CaracteristicaVehiculo, CaracteristicaVehiculoDTO, String> {
  public CaracteristicaVehiculoRestController(CaracteristicaVehiculoService service, CaracteristicaVehiculoMapper mapper) {
    super(service, mapper);
  }
}

