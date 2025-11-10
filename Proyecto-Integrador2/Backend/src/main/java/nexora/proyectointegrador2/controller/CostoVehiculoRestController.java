package nexora.proyectointegrador2.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import nexora.proyectointegrador2.business.domain.entity.CostoVehiculo;
import nexora.proyectointegrador2.business.logic.service.CostoVehiculoService;
import nexora.proyectointegrador2.utils.dto.CostoVehiculoDTO;
import nexora.proyectointegrador2.utils.mapper.impl.CostoVehiculoMapper;

@RestController
@RequestMapping("api/v1/costos-vehiculo")
public class CostoVehiculoRestController extends BaseRestController<CostoVehiculo, CostoVehiculoDTO, String> {
  public CostoVehiculoRestController(CostoVehiculoService service, CostoVehiculoMapper mapper) {
    super(service, mapper);
  }
}

