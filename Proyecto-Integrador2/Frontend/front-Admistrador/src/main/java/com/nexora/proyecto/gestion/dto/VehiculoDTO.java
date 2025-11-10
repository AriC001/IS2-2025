package com.nexora.proyecto.gestion.dto;

import com.nexora.proyecto.gestion.dto.enums.EstadoVehiculo;

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
public class VehiculoDTO extends BaseDTO {

  private EstadoVehiculo estadoVehiculo;
  private String patente;
  private CaracteristicaVehiculoDTO caracteristicaVehiculo;

}
