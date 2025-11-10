package nexora.proyectointegrador2.utils.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import nexora.proyectointegrador2.business.enums.TipoPago;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class FormaDePagoDTO extends BaseDTO {

  private TipoPago tipoPago;
  private String observacion;
}

