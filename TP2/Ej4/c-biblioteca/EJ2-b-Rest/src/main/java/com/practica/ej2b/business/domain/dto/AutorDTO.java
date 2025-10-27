package com.practica.ej2b.business.domain.dto;

import org.antlr.v4.runtime.misc.NotNull;

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

public class AutorDTO extends BaseDTO {
  
  @NotNull
  private String nombre;

  @NotNull
  private String apellido;

  @NotNull
  private String biografia;

}
