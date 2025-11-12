package nexora.proyectointegrador2.utils.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import nexora.proyectointegrador2.business.enums.TipoDocumentacion;

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
  private String mimeType;

}

