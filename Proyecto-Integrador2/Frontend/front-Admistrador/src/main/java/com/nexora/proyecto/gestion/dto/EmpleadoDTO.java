package com.nexora.proyecto.gestion.dto;

import com.nexora.proyecto.gestion.dto.enums.TipoEmpleado;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class EmpleadoDTO extends PersonaDTO {

  private TipoEmpleado tipoEmpleado;

}
