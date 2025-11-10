package nexora.proyectointegrador2.business.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
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
@Table(name = "contacto_correo_electronico", indexes = {
    @Index(name = "idx_email", columnList = "email")
})
public class ContactoCorreoElectronico extends Contacto {

  @Column(name = "email", nullable = false, unique = true, length = 100)
  private String email;

}
