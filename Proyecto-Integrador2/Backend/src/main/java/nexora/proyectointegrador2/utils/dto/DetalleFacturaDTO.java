package nexora.proyectointegrador2.utils.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class DetalleFacturaDTO extends BaseDTO {

  private Integer cantidad;
  private Double subtotal;
  private AlquilerDTO alquiler;
  private FacturaDTO factura;
}
