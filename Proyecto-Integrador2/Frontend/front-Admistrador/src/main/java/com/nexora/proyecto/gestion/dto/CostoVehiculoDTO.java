package com.nexora.proyecto.gestion.dto;

import java.util.Date;

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
public class CostoVehiculoDTO extends BaseDTO {

  private Date fechaDesde;
  private Date fechaHasta;
  private Double costo;

}
