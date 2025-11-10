package nexora.proyectointegrador2.business.domain.entity;

import java.util.Date;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Index;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nexora.proyectointegrador2.business.enums.TipoDocumentacion;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "persona", indexes = {
    @Index(name = "idx_numero_documento", columnList = "numero_documento")
})
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Persona extends BaseEntity<String> {

  @Column(nullable = false, length = 100)
  private String nombre;

  @Column(nullable = false, length = 100)
  private String apellido;

  @Temporal(TemporalType.DATE)
  @Column(nullable = false)
  private Date fechaNacimiento;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 20)
  private TipoDocumentacion tipoDocumento;

  @Column(name = "numero_documento", nullable = false, unique = true, length = 20)
  private String numeroDocumento;

  @OneToOne
  @JoinColumn(name = "usuario_id")
  private Usuario usuario;

  @ManyToOne
  @JoinColumn(name = "direccion_id")
  private Direccion direccion;

  @OneToMany(mappedBy = "persona", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Contacto> contactos; 

  @OneToOne
  @JoinColumn(name = "imagen_perfil_id")
  private Imagen imagenPerfil;

}
