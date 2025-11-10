package nexora.proyectointegrador2.utils.dto;

import java.io.Serializable;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public abstract class BaseDTO implements Serializable {

  private String id;
  private boolean eliminado;

}
