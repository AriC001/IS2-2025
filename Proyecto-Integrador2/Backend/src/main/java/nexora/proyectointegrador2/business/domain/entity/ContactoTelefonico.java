package nexora.proyectointegrador2.business.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nexora.proyectointegrador2.business.enums.TipoTelefono;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "contacto_telefonico")
public class ContactoTelefonico extends Contacto {

  @Column(name = "telefono", nullable = false, length = 20)
  private String telefono;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 20)
  private TipoTelefono tipoTelefono;

}
