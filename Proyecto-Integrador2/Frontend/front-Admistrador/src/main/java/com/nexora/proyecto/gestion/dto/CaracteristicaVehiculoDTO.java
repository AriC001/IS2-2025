package com.nexora.proyecto.gestion.dto;

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
