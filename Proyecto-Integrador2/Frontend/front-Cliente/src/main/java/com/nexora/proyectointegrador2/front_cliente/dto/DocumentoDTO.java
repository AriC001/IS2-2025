package com.nexora.proyectointegrador2.front_cliente.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.nexora.proyectointegrador2.front_cliente.dto.enums.TipoDocumentacion;

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
  private String mimeType;
  private LocalDateTime fechaCarga;
  private String urlDescarga;
}
