package com.practica.ej2b.business.domain.entity;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
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
@Table(name = "autor")
public class Autor extends BaseEntity<Long> implements Serializable {

  @Column(length=50, nullable=false)
  private String nombre;

  @Column(length=50, nullable=false)
  private String apellido;

  @Column(length=500)
  private String biografia;

  @ManyToMany(cascade= {CascadeType.PERSIST, CascadeType.MERGE})
  @JoinTable(
    name = "autor_libro",
    joinColumns = @JoinColumn(name = "autor_id"),
    inverseJoinColumns = @JoinColumn(name = "libro_id")
  )
  private Set<Libro> libros = new HashSet<>();

}
