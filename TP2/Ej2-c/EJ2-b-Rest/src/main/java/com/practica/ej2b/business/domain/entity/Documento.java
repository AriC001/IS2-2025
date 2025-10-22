package com.practica.ej2b.business.domain.entity;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Table(name = "documento")
public class Documento extends BaseEntity<Long> implements Serializable {

  @Column(nullable = false, length = 255)
  private String nombreArchivo;

  @Column(nullable = false, length = 500)
  private String rutaArchivo;

  @Column(nullable = false, length = 100)
  private String tipoContenido;

}
