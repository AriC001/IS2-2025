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
public class ClienteDTO extends PersonaDTO {

  private String direccionEstadia;
  private String nacionalidadId;

}
