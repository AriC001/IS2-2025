package com.sport.proyecto.entidades;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

@Entity
@Table(name = "empresa")
public class Empresa implements Serializable {
  // Atributos

  @Id
  private String id;

  private String nombre;

  private String telefono;

  private String correoElectronico;

  private boolean eliminado;
}
