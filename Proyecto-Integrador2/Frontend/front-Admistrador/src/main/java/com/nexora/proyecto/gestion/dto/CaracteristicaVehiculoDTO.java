package com.nexora.proyecto.gestion.dto;

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
public class CaracteristicaVehiculoDTO extends BaseDTO {

  private String marca;
  private String modelo;
  private Integer cantidadPuerta;
  private Integer cantidadAsiento;
  private Integer anio;
  private Integer cantidadTotalVehiculo;
  private Integer cantidadVehiculoDisponible;
  private ImagenDTO imagenVehiculo;
  private CostoVehiculoDTO costoVehiculo;

}
