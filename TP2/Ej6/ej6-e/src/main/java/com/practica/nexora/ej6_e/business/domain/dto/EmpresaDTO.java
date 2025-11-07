package com.practica.nexora.ej6_e.business.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class EmpresaDTO extends BaseDTO {

  private String nombre;

  private ContactoDTO contacto;
  
}
