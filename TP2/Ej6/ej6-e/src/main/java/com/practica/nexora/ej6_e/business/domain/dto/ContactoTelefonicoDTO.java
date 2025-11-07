package com.practica.nexora.ej6_e.business.domain.dto;

import com.practica.nexora.ej6_e.business.enums.TipoTelefono;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class ContactoTelefonicoDTO extends ContactoDTO {

  private String numeroTelefono;

  private TipoTelefono tipoTelefono;

}
