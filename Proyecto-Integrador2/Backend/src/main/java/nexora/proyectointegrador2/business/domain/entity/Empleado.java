package nexora.proyectointegrador2.business.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nexora.proyectointegrador2.business.enums.TipoEmpleado;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "empleado")
public class Empleado extends Persona {

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 30)
  private TipoEmpleado tipoEmpleado;

}
