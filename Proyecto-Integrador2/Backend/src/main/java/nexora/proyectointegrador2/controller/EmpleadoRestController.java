package nexora.proyectointegrador2.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import nexora.proyectointegrador2.business.domain.entity.Empleado;
import nexora.proyectointegrador2.business.logic.service.EmpleadoService;
import nexora.proyectointegrador2.utils.dto.EmpleadoDTO;
import nexora.proyectointegrador2.utils.mapper.impl.EmpleadoMapper;

@RestController
@RequestMapping("api/v1/empleados")
public class EmpleadoRestController extends BaseRestController<Empleado, EmpleadoDTO, String> {
  public EmpleadoRestController(EmpleadoService service, EmpleadoMapper mapper) {
    super(service, mapper);
  }
}

