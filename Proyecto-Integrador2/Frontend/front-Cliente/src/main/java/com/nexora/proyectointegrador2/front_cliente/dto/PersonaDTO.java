package  com.nexora.proyectointegrador2.front_cliente.dto;

import java.util.Date;
import java.util.List;

import com.nexora.proyectointegrador2.front_cliente.dto.enums.TipoDocumentacion;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(value = {"contactos"}, ignoreUnknown = true)
public abstract class PersonaDTO extends BaseDTO {

  private String nombre;
  private String apellido;
  private Date fechaNacimiento;
  private TipoDocumentacion tipoDocumento;
  private String numeroDocumento;
  private UsuarioDTO usuario;
  private DireccionDTO direccion;
  @JsonIgnore
  private transient List<ContactoDTO> contactos;
  private ImagenDTO imagenPerfil;

}
