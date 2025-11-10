package  com.nexora.proyectointegrador2.front_cliente.dto;

import java.util.Date;

import com.nexora.proyectointegrador2.front_cliente.dto.enums.TipoDocumentacion;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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
