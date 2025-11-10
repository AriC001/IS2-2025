package nexora.proyectointegrador2.business.domain.entity;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
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
@Table(name = "empresa")
public class Empresa extends BaseEntity<String> {

  @Column(nullable = false, length = 100)
  private String nombre;

  @Column(nullable = false, length = 20)
  private String telefono;

  @Column(nullable = false, unique = true, length = 100)
  private String email;

  @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
  @JoinColumn(name = "direccion_id", nullable = false)
  private Direccion direccion;

  @OneToMany(mappedBy = "empresa", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Contacto> contactos;

}
