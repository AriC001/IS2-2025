package nexora.proyectointegrador2.business.domain.entity;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
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
@Table(name = "costo_vehiculo")
public class CostoVehiculo extends BaseEntity<String> {

  @Temporal(TemporalType.DATE)
  @Column(nullable = false)
  private Date fechaDesde;

  @Temporal(TemporalType.DATE)
  @Column(nullable = false)
  private Date fechaHasta;

  @Column(nullable = false)
  private Double costo;

}
