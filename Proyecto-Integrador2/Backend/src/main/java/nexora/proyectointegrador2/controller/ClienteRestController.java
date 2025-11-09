package nexora.proyectointegrador2.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import nexora.proyectointegrador2.business.domain.entity.Cliente;
import nexora.proyectointegrador2.business.logic.service.ClienteService;
import nexora.proyectointegrador2.utils.dto.ClienteDTO;
import nexora.proyectointegrador2.utils.mapper.impl.ClienteMapper;

@RestController
@RequestMapping("api/v1/clientes")
public class ClienteRestController extends BaseRestController<Cliente, ClienteDTO, String> {
  public ClienteRestController(ClienteService service, ClienteMapper mapper) {
    super(service, mapper);
  }
}

