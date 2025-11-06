package com.nexora.proyecto.gestion.dto;

import java.util.Date;

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
public class CostoVehiculoDTO extends BaseDTO {

  private Date fechaDesde;
  private Date fechaHasta;
  private Double costo;

}
