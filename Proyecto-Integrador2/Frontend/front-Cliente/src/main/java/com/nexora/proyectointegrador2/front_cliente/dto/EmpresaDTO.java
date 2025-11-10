package  com.nexora.proyectointegrador2.front_cliente.dto;

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
public class EmpresaDTO extends BaseDTO {

  private String nombre;
  private String telefono;
  private String email;
  private DireccionDTO direccion;

}
