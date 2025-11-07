package com.practica.nexora.ej6_e.business.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class PersonaDTO extends BaseDTO {

  private String nombre;
  private String apellido;
  private UsuarioDTO usuario;

}
