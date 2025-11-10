package nexora.proyectointegrador2.business.domain.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nexora.proyectointegrador2.business.enums.EstadoFactura;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "factura")
public class Factura extends BaseEntity<String> {

  @Column(name = "numero_factura", nullable = false, unique = true)
  private Long numeroFactura;

  @Temporal(TemporalType.DATE)
  @Column(name = "fecha_factura", nullable = false)
  private Date fechaFactura;

  @Column(name = "total_pagado", nullable = false)
  private Double totalPagado;

  @Enumerated(EnumType.STRING)
  @Column(name = "estado", nullable = false, length = 30)
  private EstadoFactura estado;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "forma_pago_id", nullable = false)
  private FormaDePago formaDePago;

  @Builder.Default
  @OneToMany(mappedBy = "factura", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
  private List<DetalleFactura> detalles = new ArrayList<>();

  public void agregarDetalle(DetalleFactura detalle) {
    if (detalle != null) {
      detalle.setFactura(this);
      this.detalles.add(detalle);
    }
  }

  public void removerDetalle(DetalleFactura detalle) {
    if (detalle != null) {
      detalle.setFactura(null);
      this.detalles.remove(detalle);
    }
  }
}



