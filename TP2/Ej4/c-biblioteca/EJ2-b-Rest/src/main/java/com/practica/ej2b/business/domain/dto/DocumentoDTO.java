package com.practica.ej2b.business.domain.dto;

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
public class DocumentoDTO extends BaseDTO {

  private String nombreArchivo;
  
  private String rutaArchivo;
  
  private String tipoContenido;

}
