package com.sport.proyecto.entidades;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

@Entity
@Table(name = "direccion")
public class Direccion {
  // Atributos

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String calle;

  private String numeracion;

  private String barrio;

  private String manzanaPiso;

  private String casaDepartamento;

  private String referencia;

  private boolean eliminado;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "localidad_id")
  private Localidad localidad;
}
