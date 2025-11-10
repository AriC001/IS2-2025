package com.practica.ej2b.business.domain.entity;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name = "persona")
public class Persona extends BaseEntity<Long> implements Serializable {

  @Column(length=50)
  private String nombre;

  @Column(length=50)
  private String apellido;

  @Column(unique=true)
  private String dni;

  @ManyToOne(fetch=FetchType.EAGER, optional=false)
  private Domicilio domicilio;

}
