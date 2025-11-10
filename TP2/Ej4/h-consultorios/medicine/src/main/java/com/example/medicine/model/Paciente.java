package com.example.medicine.model;
import com.example.medicine.errors.ErrorServicio;
import com.example.medicine.model.FotoPaciente;

import lombok.*;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import com.example.medicine.errors.*;
import org.hibernate.annotations.GenericGenerator;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

@Entity
@Table(name = "paciente")
public class Paciente  implements Serializable {
  @Id
  @GeneratedValue(generator = "uuid2")
  @GenericGenerator(name = "uuid2", strategy = "uuid2")
  @Column(updatable = false, nullable = false)
  private String id;

  @Column(name = "nombre")
  private String nombre;

  @Column(name = "apellido")
  private String apellido;

  @Column(name = "documento")
  private String documento;

  @Column(name = "eliminado")
  private boolean eliminado;

  @OneToOne(mappedBy = "paciente", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  private FotoPaciente foto;

  @OneToMany(mappedBy = "paciente", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  private List<HistoriaClinica> historiasClinicas;


  public String getNombreCompleto() {
    return nombre + " " + apellido;
  }

  public boolean isActivo() {
    return !eliminado;
  }


}

