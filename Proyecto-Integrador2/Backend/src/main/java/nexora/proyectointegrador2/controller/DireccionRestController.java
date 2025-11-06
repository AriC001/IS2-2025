package nexora.proyectointegrador2.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import nexora.proyectointegrador2.business.domain.entity.Direccion;
import nexora.proyectointegrador2.business.logic.service.DireccionService;
import nexora.proyectointegrador2.utils.dto.DireccionDTO;
import nexora.proyectointegrador2.utils.mapper.impl.DireccionMapper;

@RestController
@RequestMapping("api/v1/direcciones")
public class DireccionRestController extends BaseRestController<Direccion, DireccionDTO, String> {

  public DireccionRestController(DireccionService service, DireccionMapper adapter) {
    super(service, adapter);
  }
}
