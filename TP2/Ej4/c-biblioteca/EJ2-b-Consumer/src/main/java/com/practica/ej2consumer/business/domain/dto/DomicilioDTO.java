package com.practica.ej2consumer.business.domain.dto;

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

public class DomicilioDTO extends BaseDTO {
  
  private String calle;

  private String numero;

  private LocalidadDTO localidad;

}
