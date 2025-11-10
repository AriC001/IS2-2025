package nexora.proyectointegrador2.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import nexora.proyectointegrador2.business.domain.entity.Pais;
import nexora.proyectointegrador2.business.logic.service.PaisService;
import nexora.proyectointegrador2.utils.dto.PaisDTO;
import nexora.proyectointegrador2.utils.mapper.impl.PaisMapper;

@RestController
@RequestMapping("api/v1/paises")
public class PaisRestController extends BaseRestController<Pais, PaisDTO, String> {

  public PaisRestController(PaisService service, PaisMapper adapter) {
    super(service, adapter);
  }

}
