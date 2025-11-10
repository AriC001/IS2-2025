package com.practica.ej2b.business.domain.entity;


import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name = "libro")
public class Libro extends BaseEntity<Long> implements Serializable {

  @Column(length=100)
  private String titulo;

  private String genero;

  private int paginas;

  @Temporal(TemporalType.DATE)
  private Date fecha;

  @ManyToMany(mappedBy = "libros")
  private Set<Autor> autores = new HashSet<>();

  @ManyToOne
  private Persona persona;

}
