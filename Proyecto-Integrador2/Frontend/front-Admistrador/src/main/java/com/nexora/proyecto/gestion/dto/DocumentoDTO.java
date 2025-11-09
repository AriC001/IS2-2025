package com.nexora.proyecto.gestion.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.nexora.proyecto.gestion.dto.enums.TipoDocumentacion;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DocumentoDTO extends BaseDTO {

  private TipoDocumentacion tipoDocumento;
  private String observacion;
  private String pathArchivo;
  private String nombreArchivo;

}

