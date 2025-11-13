package nexora.proyectointegrador2.utils.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import nexora.proyectointegrador2.business.enums.EstadoAlquiler;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class AlquilerDTO extends BaseDTO {

  private Date fechaDesde;
  private Date fechaHasta;
  private EstadoAlquiler estadoAlquiler;
  private ClienteDTO cliente;
  private VehiculoDTO vehiculo;
  private DocumentoDTO documento;

}

