package nexora.proyectointegrador2.utils.dto;

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
public class ConfiguracionCorreoElectronicoDTO extends BaseDTO {

  private String smtp;
  private Integer puerto;
  private String email;
  private String clave;
  private boolean tls;
  private EmpresaDTO empresa;

}
