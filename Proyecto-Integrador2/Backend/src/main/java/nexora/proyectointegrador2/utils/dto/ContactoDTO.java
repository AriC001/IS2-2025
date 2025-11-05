package nexora.proyectointegrador2.utils.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nexora.proyectointegrador2.business.enums.TipoContacto;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public abstract class ContactoDTO extends BaseDTO {

  private TipoContacto tipoContacto;
  private String observacion;

}
