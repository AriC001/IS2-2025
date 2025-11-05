package nexora.proyectointegrador2.business.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nexora.proyectointegrador2.business.enums.RolUsuario;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "usuario", indexes = {
    @Index(name = "idx_nombre_usuario", columnList = "nombre_usuario")
})
public class Usuario extends BaseEntity<String> {

  @Column(name = "nombre_usuario", unique = true, nullable = false, length = 50)
  private String nombreUsuario;

  @Column(nullable = false, length = 100)
  private String clave;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 20)
  private RolUsuario rol;
  
}
