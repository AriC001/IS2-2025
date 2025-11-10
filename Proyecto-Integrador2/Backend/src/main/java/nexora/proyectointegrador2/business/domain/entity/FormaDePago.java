package nexora.proyectointegrador2.business.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nexora.proyectointegrador2.business.enums.TipoPago;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "forma_pago")
public class FormaDePago extends BaseEntity<String> {

  @Enumerated(EnumType.STRING)
  @Column(name = "tipo_pago", nullable = false, length = 60)
  private TipoPago tipoPago;

  @Column(name = "observacion", length = 500)
  private String observacion;
}

