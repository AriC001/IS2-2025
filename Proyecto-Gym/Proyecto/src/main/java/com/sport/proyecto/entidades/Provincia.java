package com.sport.proyecto.entidades;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.io.Serializable;


@NoArgsConstructor
@AllArgsConstructor
@Builder

@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Table(name = "provincia")
public class Provincia implements Serializable {
  // Atributos

  @Id
  @GeneratedValue(generator = "UUID")
  @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
  private String id;

  private String nombre;

  private boolean eliminado;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "pais_id")
  private Pais pais;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getNombre() {
    return nombre;
  }

  public void setNombre(String nombre) {
    this.nombre = nombre;
  }

  public boolean isEliminado() {
    return eliminado;
  }

  public void setEliminado(boolean eliminado) {
    this.eliminado = eliminado;
  }

  public Pais getPais() {
    return pais;
  }

  public void setPais(Pais pais) {
    this.pais = pais;
  }

  

}
