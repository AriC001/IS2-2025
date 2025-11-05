package nexora.proyectointegrador2.business.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nexora.proyectointegrador2.business.enums.EstadoVehiculo;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "vehiculo", indexes = {
    @Index(name = "idx_patente", columnList = "patente")
})
public class Vehiculo extends BaseEntity<String> {

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 30)
  private EstadoVehiculo estadoVehiculo;

  @Column(nullable = false, unique = true, length = 20)
  private String patente;

  @ManyToOne
  @JoinColumn(name = "caracteristica_vehiculo_id", nullable = false)
  private CaracteristicaVehiculo caracteristicaVehiculo;

}
