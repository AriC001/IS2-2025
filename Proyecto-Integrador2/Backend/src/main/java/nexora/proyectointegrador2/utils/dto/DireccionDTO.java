package nexora.proyectointegrador2.utils.dto;

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
public class DireccionDTO extends BaseDTO {

  private String calle;
  private String numero;
  private String barrio;
  private String manzanaPiso;
  private String casaDepartamento;
  private String referencia;
  private LocalidadDTO localidad;

}
