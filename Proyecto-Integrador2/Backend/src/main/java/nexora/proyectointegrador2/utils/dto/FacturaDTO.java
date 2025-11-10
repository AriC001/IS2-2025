package nexora.proyectointegrador2.utils.dto;

import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import nexora.proyectointegrador2.business.enums.EstadoFactura;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class FacturaDTO extends BaseDTO {

  private Long numeroFactura;
  private Date fechaFactura;
  private Double totalPagado;
  private EstadoFactura estado;
  private FormaDePagoDTO formaDePago;
  private List<DetalleFacturaDTO> detalles;
}
