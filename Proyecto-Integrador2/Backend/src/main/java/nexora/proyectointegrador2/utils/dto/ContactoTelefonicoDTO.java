package nexora.proyectointegrador2.utils.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nexora.proyectointegrador2.business.enums.TipoTelefono;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContactoTelefonicoDTO extends ContactoDTO {

  private String telefono;
  private TipoTelefono tipoTelefono;

}
