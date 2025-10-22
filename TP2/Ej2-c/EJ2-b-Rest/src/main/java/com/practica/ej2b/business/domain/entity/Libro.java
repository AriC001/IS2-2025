package com.practica.ej2b.business.domain.entity;


import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
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

  @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER)
  @JoinTable(
    name = "autor_libro",
    joinColumns = @JoinColumn(name = "libro_id"),
    inverseJoinColumns = @JoinColumn(name = "autor_id")
  )
  private Set<Autor> autores = new HashSet<>();

  @ManyToOne
  private Persona persona;

  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "documento_id", referencedColumnName = "id")
  private Documento documento; 

  // Métodos helper para mantener la bidireccionalidad sincronizada
  public void addAutor(Autor autor) {
    this.autores.add(autor);
    autor.getLibros().add(this);
  }

  public void removeAutor(Autor autor) {
    this.autores.remove(autor);
    autor.getLibros().remove(this);
  }

}
