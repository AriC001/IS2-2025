package nexora.proyectointegrador2.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import nexora.proyectointegrador2.business.domain.entity.Usuario;
import nexora.proyectointegrador2.business.logic.service.UsuarioService;
import nexora.proyectointegrador2.utils.dto.UsuarioDTO;
import nexora.proyectointegrador2.utils.mapper.impl.UsuarioMapper;

@RestController
@RequestMapping("api/v1/usuarios")
public class UsuarioRestController extends BaseRestController<Usuario, UsuarioDTO, String> {
  
  private static final Logger logger = LoggerFactory.getLogger(UsuarioRestController.class);
  private final PasswordEncoder passwordEncoder;

  public UsuarioRestController(UsuarioService service, UsuarioMapper adapter, PasswordEncoder passwordEncoder) {
    super(service, adapter);
    this.passwordEncoder = passwordEncoder;
  }

  /**
   * Endpoint para cambiar la contraseña de un usuario.
   * 
   * @param id ID del usuario
   * @param cambioClaveRequest DTO con la nueva contraseña
   * @return ResponseEntity con mensaje de éxito
   */
  @PostMapping("/{id}/cambiar-clave")
  public ResponseEntity<String> cambiarClave(
      @PathVariable String id,
      @RequestBody CambioClaveRequest cambioClaveRequest) throws Exception {
    
    logger.info("Solicitud de cambio de contraseña para usuario ID: {}", id);
    
    if (cambioClaveRequest == null || cambioClaveRequest.getNuevaClave() == null || 
        cambioClaveRequest.getNuevaClave().trim().isEmpty()) {
      throw new Exception("La nueva contraseña es obligatoria");
    }
    
    if (cambioClaveRequest.getNuevaClave().length() < 4) {
      throw new Exception("La contraseña debe tener al menos 4 caracteres");
    }
    
    // Obtener el usuario
    Usuario usuario = service.findById(id);
    
    // Validar que no sea la contraseña por defecto (comparar directamente ya que viene en texto plano)
    if ("mycar".equals(cambioClaveRequest.getNuevaClave())) {
      throw new Exception("No se puede usar la contraseña por defecto");
    }
    
    // Encriptar y actualizar la contraseña
    usuario.setClave(passwordEncoder.encode(cambioClaveRequest.getNuevaClave()));
    service.update(id, usuario);
    
    logger.info("✅ Contraseña cambiada exitosamente para usuario ID: {}", id);
    
    return ResponseEntity.ok("Contraseña cambiada exitosamente");
  }

  /**
   * DTO interno para la solicitud de cambio de contraseña.
   */
  public static class CambioClaveRequest {
    private String nuevaClave;

    public String getNuevaClave() {
      return nuevaClave;
    }

    public void setNuevaClave(String nuevaClave) {
      this.nuevaClave = nuevaClave;
    }
  }
}
