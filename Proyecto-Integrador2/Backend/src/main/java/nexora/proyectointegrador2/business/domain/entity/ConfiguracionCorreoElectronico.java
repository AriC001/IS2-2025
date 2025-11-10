package nexora.proyectointegrador2.business.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
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
@Table(name = "configuracion_correo_electronico")
public class ConfiguracionCorreoElectronico extends BaseEntity<String> {

  @Column(nullable = false, length = 100)
  private String smtp;

  @Column(nullable = false)
  private Integer puerto;

  @Column(nullable = false, length = 100)
  private String email;

  @Column(nullable = false, length = 100)
  private String clave;

  @Builder.Default
  @Column(nullable = false)
  private boolean tls = true;

  @OneToOne
  @JoinColumn(name = "empresa_id", nullable = false)
  private Empresa empresa;

}
