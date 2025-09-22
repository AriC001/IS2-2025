package com.sport.proyecto.entidades;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Setter
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder

@Entity
@Table(name = "pais")
public class Pais implements Serializable {
  // Atributos

  @Id
  private String id;

  private String nombre;

  private boolean eliminado;

}
