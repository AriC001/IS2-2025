package nexora.proyectointegrador2.business.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import org.hibernate.annotations.Formula;
import java.util.ArrayList;
import java.util.List;
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
  // Calculated based on how many Vehiculo entities reference this CaracteristicaVehiculo
  // Removed persistent column and expose as a calculated property below.
  @OneToMany(mappedBy = "caracteristicaVehiculo")
  @Builder.Default
  private List<Vehiculo> vehiculos = new ArrayList<>();
  // Calculated in DB to avoid lazy-loading / stale-value issues
  @Formula("(select count(*) from vehiculo v where v.caracteristica_vehiculo_id = id)")
  private Integer cantidadTotalVehiculo;

  // Calculated in DB: number of associated Vehiculo rows that are not eliminado and are DISPONIBLE
  @Formula("(select count(*) from vehiculo v where v.caracteristica_vehiculo_id = id and v.eliminado = 0 and v.estado_vehiculo = 'DISPONIBLE')")
  private Integer cantidadVehiculoDisponible;

  @ManyToOne
  @JoinColumn(name = "imagen_vehiculo_id")
  private Imagen imagenVehiculo;

  @ManyToOne
  @JoinColumn(name = "costo_vehiculo_id", nullable = false)
  private CostoVehiculo costoVehiculo;

  // getCantidadTotalVehiculo() provided by Lombok via the field above (@Formula)

}
