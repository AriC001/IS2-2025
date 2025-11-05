package nexora.proyectointegrador2.business.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
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
@Entity
@Table(name = "departamento")
public class Departamento extends BaseEntity<String> {

  @Column(nullable = false, length = 100)
  private String nombre;

  @ManyToOne
  @JoinColumn(name = "provincia_id", nullable = false)
  private Provincia provincia;

}
