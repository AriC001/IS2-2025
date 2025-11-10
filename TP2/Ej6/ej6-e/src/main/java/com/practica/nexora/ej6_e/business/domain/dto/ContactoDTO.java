package com.practica.nexora.ej6_e.business.domain.dto;

import com.practica.nexora.ej6_e.business.enums.TipoContacto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class ContactoDTO extends BaseDTO {

  private TipoContacto tipoContacto;

  private String observacion;

  private PersonaDTO persona;

}
