package  com.nexora.proyectointegrador2.front_cliente.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public abstract class BaseDTO implements Serializable {

  private String id;
  private boolean eliminado;

}
