package nexora.proyectointegrador2.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import nexora.proyectointegrador2.business.domain.entity.Localidad;
import nexora.proyectointegrador2.business.logic.service.LocalidadService;
import nexora.proyectointegrador2.utils.dto.LocalidadDTO;
import nexora.proyectointegrador2.utils.mapper.impl.LocalidadMapper;

@RestController
@RequestMapping("api/v1/localidades")
public class LocalidadRestController extends BaseRestController<Localidad, LocalidadDTO, String> {
  public LocalidadRestController(LocalidadService service, LocalidadMapper adapter) {
    super(service, adapter);
  }
}
