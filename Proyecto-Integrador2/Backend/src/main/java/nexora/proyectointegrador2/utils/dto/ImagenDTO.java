package nexora.proyectointegrador2.utils.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import nexora.proyectointegrador2.business.enums.TipoImagen;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class ImagenDTO extends BaseDTO {

  private String nombre;
  private String mime;
  private byte[] contenido;
  private TipoImagen tipoImagen;

}
