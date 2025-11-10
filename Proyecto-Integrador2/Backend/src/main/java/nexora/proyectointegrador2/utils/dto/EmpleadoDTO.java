package nexora.proyectointegrador2.utils.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import nexora.proyectointegrador2.business.enums.TipoEmpleado;

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class EmpleadoDTO extends PersonaDTO {

  private TipoEmpleado tipoEmpleado;

}
