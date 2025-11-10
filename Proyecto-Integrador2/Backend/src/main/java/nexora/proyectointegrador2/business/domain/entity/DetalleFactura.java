package nexora.proyectointegrador2.business.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
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
@Table(name = "detalle_factura")
public class DetalleFactura extends BaseEntity<String> {

  @Column(name = "cantidad", nullable = false)
  private Integer cantidad;

  @Column(name = "subtotal", nullable = false)
  private Double subtotal;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "alquiler_id", nullable = false)
  private Alquiler alquiler;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "factura_id", nullable = false)
  private Factura factura;
}


