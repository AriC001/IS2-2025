package com.example.medicine.model;
import lombok.*;
import jakarta.persistence.*;
import java.io.Serializable;

import org.hibernate.annotations.GenericGenerator;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

@Entity
@Table(name = "medico")
public class Medico implements Serializable {

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

  public String getNombreCompleto() {
    return nombre + " " + apellido;
  }

  public boolean isActivo() {
    return !eliminado;
  }
}

