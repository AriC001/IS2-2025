package com.nexora.proyecto.gestion.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class DireccionDTO extends BaseDTO {

  private String calle;
  private String numero;
  private String barrio;
  private String manzanaPiso;
  private String casaDepartamento;
  private String referencia;
  private LocalidadDTO localidad;

}
