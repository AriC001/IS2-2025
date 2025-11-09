package com.nexora.proyecto.gestion.dto;

import com.nexora.proyecto.gestion.dto.enums.EstadoVehiculo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
