package nexora.proyectointegrador2.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import nexora.proyectointegrador2.business.domain.entity.Usuario;
import nexora.proyectointegrador2.business.logic.service.UsuarioService;
import nexora.proyectointegrador2.utils.dto.UsuarioDTO;
import nexora.proyectointegrador2.utils.mapper.impl.UsuarioMapper;

@RestController
@RequestMapping("api/v1/usuarios")
public class UsuarioRestController extends BaseRestController<Usuario, UsuarioDTO, String> {
  public UsuarioRestController(UsuarioService service, UsuarioMapper adapter) {
    super(service, adapter);
  }
}
