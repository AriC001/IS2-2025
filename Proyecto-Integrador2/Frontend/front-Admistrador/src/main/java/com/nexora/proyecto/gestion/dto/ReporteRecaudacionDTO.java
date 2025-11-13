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
public class ReporteRecaudacionDTO {

  private String modeloVehiculo;
  private String marcaVehiculo;
  private Double totalRecaudado;
  private Long cantidadAlquileres;

}

