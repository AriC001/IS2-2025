package com.practica.ej1b.business.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
@Getter
@Setter

@Entity
@Table(name = "usuario")
public class Usuario extends Persona implements Serializable {

  @Column(nullable = false)
  private String nombreUsuario;

  @Column(nullable = false)
  private String contrasenia;

}
