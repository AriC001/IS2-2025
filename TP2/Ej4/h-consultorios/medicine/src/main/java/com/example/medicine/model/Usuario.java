package com.example.medicine.model;
import lombok.*;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.List;
import org.hibernate.annotations.GenericGenerator;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

@Entity
@Table(name = "usuario")
public class Usuario implements Serializable {

  @Id
  @GeneratedValue(generator = "uuid2")
  @GenericGenerator(name = "uuid2", strategy = "uuid2")
  @Column(updatable = false, nullable = false)
  private String id;

  @Column(name = "nombre_usuario")
  private String nombreUsuario;

  private String clave;

  private boolean eliminado;

  @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<HistoriaClinica> historiasClinicas;

  public boolean isActivo() {
    return !eliminado;
  }
}

