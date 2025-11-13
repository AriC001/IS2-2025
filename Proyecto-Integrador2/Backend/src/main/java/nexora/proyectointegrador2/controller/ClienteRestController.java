package nexora.proyectointegrador2.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import nexora.proyectointegrador2.business.domain.entity.Cliente;
import nexora.proyectointegrador2.business.logic.service.ClienteService;
import nexora.proyectointegrador2.utils.dto.ClienteDTO;
import nexora.proyectointegrador2.utils.mapper.impl.ClienteMapper;

@RestController
@RequestMapping("api/v1/clientes")
public class ClienteRestController extends BaseRestController<Cliente, ClienteDTO, String> {
  
  private final ClienteService clienteService;
  
  public ClienteRestController(ClienteService service, ClienteMapper mapper) {
    super(service, mapper);
    this.clienteService = service;
  }

  /**
   * Busca un cliente por nombre de usuario.
   * 
   * @param nombreUsuario nombre de usuario
   * @return ClienteDTO asociado al usuario, o 404 si no existe
   * @throws Exception si ocurre un error
   */
  @GetMapping("/por-usuario/{nombreUsuario}")
  public ResponseEntity<ClienteDTO> findByNombreUsuario(@PathVariable String nombreUsuario) throws Exception {
    Cliente cliente = clienteService.findByNombreUsuario(nombreUsuario);
    if (cliente == null) {
      return ResponseEntity.notFound().build();
    }
    ClienteDTO dto = mapper.toDTO(cliente);
    return ResponseEntity.ok(dto);
  }
}

