package com.sport.proyecto.entidades;

import com.sport.proyecto.enums.Rol;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;


@NoArgsConstructor
@AllArgsConstructor
@Builder

@Entity
@Table(name = "usuario")
public class Usuario implements Serializable {

  @Id
  @GeneratedValue(generator = "UUID")
  @Column(updatable = false, nullable = false)
  private String id;

  @Column(name = "nombre_usuario")
  private String nombreUsuario;

  private String clave;

  @Enumerated(EnumType.STRING)
  private Rol rol;

  private boolean eliminado;

  public String getId() {
    return id;
}

  public void setId(String id) {
    this.id = id;
  }

  public String getNombreUsuario() {
    return nombreUsuario;
  }

  public void setNombreUsuario(String nombreUsuario) {
    this.nombreUsuario = nombreUsuario;
  }

  public String getClave() {
    return clave;
  }

  public void setClave(String clave) {
    this.clave = clave;
  }

  public Rol getRol() {
    return rol;
  }

  public void setRol(Rol rol) {
    this.rol = rol;
  }

  public boolean isEliminado() {
    return eliminado;
  }

  public void setEliminado(boolean eliminado) {
    this.eliminado = eliminado;
  }

  public String getEmail(){
        return this.nombreUsuario;
    }

    public String getPassword(){
        return this.clave;
    }
}
