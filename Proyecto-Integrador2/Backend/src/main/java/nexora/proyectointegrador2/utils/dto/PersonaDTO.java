package nexora.proyectointegrador2.utils.dto;

import java.util.Date;
import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import nexora.proyectointegrador2.business.enums.TipoDocumentacion;

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public abstract class PersonaDTO extends BaseDTO {

  private String nombre;
  private String apellido;
  private Date fechaNacimiento;
  private TipoDocumentacion tipoDocumento;
  private String numeroDocumento;
  private UsuarioDTO usuario;
  private DireccionDTO direccion;
  private List<ContactoDTO> contactos;
  private ImagenDTO imagenPerfil;

}
