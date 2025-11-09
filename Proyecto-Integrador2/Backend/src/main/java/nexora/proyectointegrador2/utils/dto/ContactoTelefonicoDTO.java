package nexora.proyectointegrador2.utils.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import nexora.proyectointegrador2.business.enums.TipoTelefono;

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class ContactoTelefonicoDTO extends ContactoDTO {

  private String telefono;
  private TipoTelefono tipoTelefono;

}
