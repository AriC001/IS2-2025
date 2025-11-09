package nexora.proyectointegrador2.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import nexora.proyectointegrador2.business.domain.entity.Provincia;
import nexora.proyectointegrador2.business.logic.service.ProvinciaService;
import nexora.proyectointegrador2.utils.dto.ProvinciaDTO;
import nexora.proyectointegrador2.utils.mapper.impl.ProvinciaMapper;

@RestController
@RequestMapping("api/v1/provincias")
public class ProvinciaRestController extends BaseRestController<Provincia, ProvinciaDTO, String> {

  public ProvinciaRestController(ProvinciaService service, ProvinciaMapper adapter) {
    super(service, adapter);
  }
}

