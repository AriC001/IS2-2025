package com.practica.nexora.ej6_e.business.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class UsuarioDTO extends BaseDTO {

  private String nombreUsuario;
  private String clave;
  private PersonaDTO persona;


}
