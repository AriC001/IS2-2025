package nexora.proyectointegrador2.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import nexora.proyectointegrador2.business.domain.entity.Empresa;
import nexora.proyectointegrador2.business.logic.service.EmpresaService;
import nexora.proyectointegrador2.utils.dto.EmpresaDTO;
import nexora.proyectointegrador2.utils.mapper.impl.EmpresaMapper;

@RestController
@RequestMapping("api/v1/empresas")
public class EmpresaRestController extends BaseRestController<Empresa, EmpresaDTO, String> {
  public EmpresaRestController(EmpresaService service, EmpresaMapper mapper) {
    super(service, mapper);
  }
}

