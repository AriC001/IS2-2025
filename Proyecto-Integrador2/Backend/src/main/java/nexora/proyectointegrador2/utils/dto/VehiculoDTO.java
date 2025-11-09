package nexora.proyectointegrador2.utils.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nexora.proyectointegrador2.business.enums.EstadoVehiculo;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VehiculoDTO extends BaseDTO {

  private EstadoVehiculo estadoVehiculo;
  private String patente;
  private CaracteristicaVehiculoDTO caracteristicaVehiculo;

}
