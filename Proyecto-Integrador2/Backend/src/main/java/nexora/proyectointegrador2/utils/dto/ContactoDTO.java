package nexora.proyectointegrador2.utils.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import nexora.proyectointegrador2.business.enums.TipoContacto;

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public abstract class ContactoDTO extends BaseDTO {

  private TipoContacto tipoContacto;
  private String observacion;

}
