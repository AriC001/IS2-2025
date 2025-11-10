package nexora.proyectointegrador2.business.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nexora.proyectointegrador2.business.enums.TipoContacto;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "contacto")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Contacto extends BaseEntity<String> {

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 30)
  private TipoContacto tipoContacto;
  
  @Column(length = 500)
  private String observacion;

  @ManyToOne
  @JoinColumn(name = "persona_id")
  private Persona persona;

  @ManyToOne
  @JoinColumn(name = "empresa_id")
  private Empresa empresa;

}
