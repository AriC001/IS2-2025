package  com.nexora.proyectointegrador2.front_cliente.dto;

import com.nexora.proyectointegrador2.front_cliente.dto.enums.RolUsuario;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsuarioDTO extends BaseDTO {

  private String nombreUsuario;
  private String clave;
  private RolUsuario rol;

  /**
   * Compatibility getters used by templates that expect `nombre`, `apellido` and `email`.
   * Some existing Thymeleaf templates reference session.usuariosession.nombre etc.
   * Provide simple mappings to avoid template parsing errors.
   */
  public String getNombre() {
    return this.nombreUsuario;
  }

  public String getApellido() {
    // Not available in this DTO; return empty string for compatibility
    return "";
  }

  public String getEmail() {
    // Email is not part of this DTO; return empty string for compatibility
    return "";
  }

}
