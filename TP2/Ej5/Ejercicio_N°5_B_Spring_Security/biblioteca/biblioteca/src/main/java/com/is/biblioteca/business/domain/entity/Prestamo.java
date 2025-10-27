package com.is.biblioteca.business.domain.entity;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;


@Entity
public class Prestamo {

  @Id
  private String id;


  @DateTimeFormat(pattern = "yyyy-MM-dd")
  private Date fechaPrestamo;

  @DateTimeFormat(pattern = "yyyy-MM-dd")
  private Date fechaDevolucion;

  private boolean eliminado;

  @ManyToOne
  private Usuario usuario;

  @OneToOne
  private Libro libro;

  public Prestamo() {
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public Date getFechaPrestamo() {
    return fechaPrestamo;
  }

  public void setFechaPrestamo(Date fechaPrestamo) {
    this.fechaPrestamo = fechaPrestamo;
  }

  public Date getFechaDevolucion() {
    return fechaDevolucion;
  }

  public void setFechaDevolucion(Date fechaDevolucion) {
    this.fechaDevolucion = fechaDevolucion;
  }

  public boolean isEliminado() {
    return eliminado;
  }

  public void setEliminado(boolean eliminado) {
    this.eliminado = eliminado;
  }

  public Usuario getUsuario() {
    return usuario;
  }

  public void setUsuario(Usuario usuario) {
    this.usuario = usuario;
  }

  public Libro getLibro() {
    return libro;
  }

  public void setLibro(Libro libro) {
    this.libro = libro;
  }

  

}
