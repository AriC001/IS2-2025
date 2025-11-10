package com.practica.ej2b.business.domain.dto;

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

public class PersonaDTO extends BaseDTO {
  
  private String nombre;

  private String apellido;

  private String dni;

  private DomicilioDTO domicilio;

}
