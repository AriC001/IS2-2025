package com.practica.ej2b.business.domain.entity;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@JsonIgnoreProperties({"libros"})
public class Autor extends BaseEntity<Long> implements Serializable {

  @Column(length=50, nullable=false)
  private String nombre;

  @Column(length=50, nullable=false)
  private String apellido;

  @Column(length=500)
  private String biografia;

  @ManyToMany(mappedBy = "autores")
  private Set<Libro> libros = new HashSet<>();

  // Métodos helper para mantener la bidireccionalidad sincronizada
  public void addLibro(Libro libro) {
    this.libros.add(libro);
    libro.getAutores().add(this);
  }

  public void removeLibro(Libro libro) {
    this.libros.remove(libro);
    libro.getAutores().remove(this);
  }

}
