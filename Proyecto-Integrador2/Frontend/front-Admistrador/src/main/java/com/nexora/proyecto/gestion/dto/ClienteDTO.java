package com.nexora.proyecto.gestion.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class ClienteDTO extends PersonaDTO {

  private String direccionEstadia;
  private NacionalidadDTO nacionalidad;

}
