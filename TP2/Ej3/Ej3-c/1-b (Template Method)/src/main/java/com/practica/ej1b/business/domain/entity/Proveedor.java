package com.practica.ej1b.business.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

@Entity
@Table(name = "proveedor")
public class Proveedor extends Persona {

  @Column(nullable = false, unique = true, length = 20)
  private String cuit;

  @ManyToOne
  private Direccion direccion;

}
