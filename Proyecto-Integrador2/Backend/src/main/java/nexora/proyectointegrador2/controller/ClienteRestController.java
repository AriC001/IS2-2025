package nexora.proyectointegrador2.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
  
  private static final Logger logger = LoggerFactory.getLogger(ClienteRestController.class);
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
    try {
      logger.debug("Buscando cliente por nombreUsuario: {}", nombreUsuario);
      Cliente cliente = clienteService.findByNombreUsuario(nombreUsuario);
      if (cliente == null) {
        logger.debug("No se encontró cliente para nombreUsuario: {}", nombreUsuario);
        return ResponseEntity.notFound().build();
      }
      logger.debug("Cliente encontrado con ID: {}, convirtiendo a DTO...", cliente.getId());
      ClienteDTO dto = mapper.toDTO(cliente);
      logger.debug("DTO convertido exitosamente");
      return ResponseEntity.ok(dto);
    } catch (Exception e) {
      logger.error("Error al buscar cliente por nombreUsuario {}: {}", nombreUsuario, e.getMessage(), e);
      throw e;
    }
  }
}

