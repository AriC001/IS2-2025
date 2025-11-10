package com.nexora.proyecto.gestion.dto;

import com.nexora.proyecto.gestion.dto.enums.TipoContacto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public abstract class ContactoDTO extends BaseDTO {

  private TipoContacto tipoContacto;
  private String observacion;

}
