package  com.nexora.proyectointegrador2.front_cliente.dto;

import com.nexora.proyectointegrador2.front_cliente.dto.enums.TipoImagen;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.Base64;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ImagenDTO extends BaseDTO {

  private String nombre;
  private String mime;
  private byte[] contenido;
  private TipoImagen tipoImagen;

  /**
   * Devuelve la imagen como data URI base64 lista para usar en el atributo src de una etiqueta <img>.
   * Si no hay contenido, devuelve null.
   */
  public String getBase64() {
    if (this.contenido == null || this.contenido.length == 0) {
      return null;
    }
    String detectedMime = (this.mime == null || this.mime.isBlank()) ? "image/jpeg" : this.mime;
    String b64 = Base64.getEncoder().encodeToString(this.contenido);
    return "data:" + detectedMime + ";base64," + b64;
  }

}
