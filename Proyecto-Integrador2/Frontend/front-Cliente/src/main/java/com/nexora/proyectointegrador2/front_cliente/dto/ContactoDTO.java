package com.nexora.proyectointegrador2.front_cliente.dto;

import com.nexora.proyectointegrador2.front_cliente.dto.enums.TipoContacto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public abstract class ContactoDTO extends BaseDTO {

  private TipoContacto tipoContacto;
  private String observacion;

}
