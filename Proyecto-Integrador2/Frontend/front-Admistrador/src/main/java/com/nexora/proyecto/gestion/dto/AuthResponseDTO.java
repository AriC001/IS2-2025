package com.nexora.proyecto.gestion.dto;

import com.nexora.proyecto.gestion.dto.enums.RolUsuario;

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
public class AuthResponseDTO {

  private String token;
  private String tipo;
  private String id;
  private String nombreUsuario;
  private RolUsuario rol;

}
