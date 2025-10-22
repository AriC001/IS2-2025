package com.practica.ej1b.business.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
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
@Table(name = "persona")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class Persona extends BaseEntity<String> {

  @Column(nullable = false, length = 50)
  private String nombre;

  @Column(nullable = false, length = 50)
  private String apellido;

  @Column(length = 20)
  private String telefono;

  @Column(unique = true, nullable = false, length = 100)
  private String correoElectronico;


}
