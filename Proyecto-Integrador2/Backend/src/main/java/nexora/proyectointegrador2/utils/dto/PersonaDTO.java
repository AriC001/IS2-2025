package nexora.proyectointegrador2.utils.dto;

import java.util.Date;

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
  private String usuarioId;
  private DireccionDTO direccion;
  private ContactoDTO contacto;
  private String imagenPerfilId;

}
