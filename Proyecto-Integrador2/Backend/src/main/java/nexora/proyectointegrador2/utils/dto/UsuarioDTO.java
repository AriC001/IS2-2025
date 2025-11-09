package nexora.proyectointegrador2.utils.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nexora.proyectointegrador2.business.enums.RolUsuario;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsuarioDTO extends BaseDTO {

  private String nombreUsuario;
  private String clave;
  private RolUsuario rol;

}
