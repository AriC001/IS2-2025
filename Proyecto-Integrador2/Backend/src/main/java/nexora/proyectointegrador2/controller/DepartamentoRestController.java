package nexora.proyectointegrador2.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import nexora.proyectointegrador2.business.domain.entity.Departamento;
import nexora.proyectointegrador2.business.logic.service.DepartamentoService;
import nexora.proyectointegrador2.utils.dto.DepartamentoDTO;
import nexora.proyectointegrador2.utils.mapper.impl.DepartamentoMapper;

@RestController
@RequestMapping("api/v1/departamentos")
public class DepartamentoRestController extends BaseRestController<Departamento, DepartamentoDTO, String> {
  public DepartamentoRestController(DepartamentoService service, DepartamentoMapper adapter) {
    super(service, adapter);
  }
}
