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
public class ConfiguracionCorreoElectronicoDTO extends BaseDTO {

  private String smtp;
  private Integer puerto;
  private String email;
  private String clave;
  private boolean tls;
  private String empresaId;

}
