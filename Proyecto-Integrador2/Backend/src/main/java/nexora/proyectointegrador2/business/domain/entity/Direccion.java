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
@Table(name = "direccion")
public class Direccion extends BaseEntity<String> {

  @Column(nullable = false, length = 100)
  private String calle;

  @Column(nullable = false, length = 10)
  private String numero;

  @Column(length = 100)
  private String barrio;

  @Column(length = 50)
  private String manzanaPiso;

  @Column(length = 50)
  private String casaDepartamento;
  
  @Column(length = 200)
  private String referencia;

  @ManyToOne
  @JoinColumn(name = "localidad_id", nullable = false)
  private Localidad localidad;

}