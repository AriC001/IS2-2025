package com.practica.ej1b.business.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter

@Entity
@Table(name = "proveedor")
public class Proveedor extends Persona implements Serializable {

  @Column(nullable = false, unique = true, length = 20)
  private String cuit;

  @ManyToOne
  private Direccion direccion;

}
