package nexora.proyectointegrador2.business.domain.entity;

import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Table(name = "alquiler")
public class Alquiler extends BaseEntity<String> {

  @Temporal(TemporalType.DATE)
  private Date fechaDesde;

  @Temporal(TemporalType.DATE)
  private Date fechaHasta;

  @ManyToOne
  @JoinColumn(name = "vehiculo_id", nullable = false)
  private Vehiculo vehiculo;

  @ManyToOne
  @JoinColumn(name = "cliente_id", nullable = false)
  private Cliente cliente;

  @ManyToOne
  private Documento documento;
}
