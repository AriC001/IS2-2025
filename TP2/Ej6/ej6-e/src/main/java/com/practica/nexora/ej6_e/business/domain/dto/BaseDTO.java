package com.practica.nexora.ej6_e.business.domain.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public abstract class BaseDTO implements Serializable {

  private Long id;
  private boolean eliminado;

}
