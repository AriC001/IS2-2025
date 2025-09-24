package com.sport.proyecto.entidades;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

@Entity
@Table(name = "sucursal")
public class Sucursal implements Serializable {
  // Atributos

  @Id
  @GeneratedValue(generator = "UUID")
  @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
  private String id;

  private String nombre;

  @ManyToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "direccion_id")
  private Direccion direccion;

  @ManyToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "empresa_id")
  private Empresa empresa;

  private boolean eliminado;
}
