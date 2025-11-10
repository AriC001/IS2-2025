package nexora.proyectointegrador2.utils.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import nexora.proyectointegrador2.business.enums.EstadoVehiculo;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class VehiculoDTO extends BaseDTO {

  private EstadoVehiculo estadoVehiculo;
  private String patente;
  private CaracteristicaVehiculoDTO caracteristicaVehiculo;

}
