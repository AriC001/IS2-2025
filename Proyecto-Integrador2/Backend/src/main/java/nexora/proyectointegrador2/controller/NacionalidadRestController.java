package nexora.proyectointegrador2.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import nexora.proyectointegrador2.business.domain.entity.Nacionalidad;
import nexora.proyectointegrador2.business.logic.service.NacionalidadService;
import nexora.proyectointegrador2.utils.dto.NacionalidadDTO;
import nexora.proyectointegrador2.utils.mapper.impl.NacionalidadMapper;

@RestController
@RequestMapping("api/v1/nacionalidades")
public class NacionalidadRestController extends BaseRestController<Nacionalidad, NacionalidadDTO, String> {
  public NacionalidadRestController(NacionalidadService service, NacionalidadMapper mapper) {
    super(service, mapper);
  }
}

