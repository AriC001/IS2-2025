package nexora.proyectointegrador2.utils.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nexora.proyectointegrador2.business.enums.TipoDocumentacion;

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

