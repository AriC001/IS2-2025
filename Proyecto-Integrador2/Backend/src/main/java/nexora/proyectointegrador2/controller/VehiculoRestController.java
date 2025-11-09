package nexora.proyectointegrador2.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import nexora.proyectointegrador2.business.domain.entity.Vehiculo;
import nexora.proyectointegrador2.business.logic.service.VehiculoService;
import nexora.proyectointegrador2.utils.dto.VehiculoDTO;
import nexora.proyectointegrador2.utils.mapper.impl.VehiculoMapper;

@RestController
@RequestMapping("api/v1/vehiculos")
public class VehiculoRestController extends BaseRestController<Vehiculo, VehiculoDTO, String> {
  public VehiculoRestController(VehiculoService service, VehiculoMapper mapper) {
    super(service, mapper);
  }
}

