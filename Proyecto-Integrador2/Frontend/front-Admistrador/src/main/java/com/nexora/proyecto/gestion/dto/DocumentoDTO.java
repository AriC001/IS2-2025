package com.nexora.proyecto.gestion.dto;

import com.nexora.proyecto.gestion.dto.enums.TipoDocumentacion;

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
public class DocumentoDTO extends BaseDTO {

  private TipoDocumentacion tipoDocumento;
  private String observacion;
  private String pathArchivo;
  private String nombreArchivo;

}

