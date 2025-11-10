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
@Table(name = "caracteristica_vehiculo")
public class CaracteristicaVehiculo extends BaseEntity<String> {

  @Column(nullable = false, length = 50)
  private String marca;

  @Column(nullable = false, length = 50)
  private String modelo;

  @Column(nullable = false)
  private Integer cantidadPuerta;

  @Column(nullable = false)
  private Integer cantidadAsiento;

  @Column(nullable = false)
  private Integer anio;

  @Column(nullable = false)
  private Integer cantidadTotalVehiculo;

  @Column(nullable = false)
  private Integer cantidadVehiculoDisponible;

  @ManyToOne
  @JoinColumn(name = "imagen_vehiculo_id")
  private Imagen imagenVehiculo;

  @ManyToOne
  @JoinColumn(name = "costo_vehiculo_id", nullable = false)
  private CostoVehiculo costoVehiculo;

}
