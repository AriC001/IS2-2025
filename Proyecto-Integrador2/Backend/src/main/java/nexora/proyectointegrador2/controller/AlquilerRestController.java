package nexora.proyectointegrador2.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import nexora.proyectointegrador2.business.domain.entity.Alquiler;
import nexora.proyectointegrador2.business.logic.service.AlquilerService;
import nexora.proyectointegrador2.utils.dto.AlquilerDTO;
import nexora.proyectointegrador2.utils.mapper.impl.AlquilerMapper;

@RestController
@RequestMapping("api/v1/alquileres")
public class AlquilerRestController extends BaseRestController<Alquiler, AlquilerDTO, String> {
  public AlquilerRestController(AlquilerService service, AlquilerMapper mapper) {
    super(service, mapper);
  }
}

