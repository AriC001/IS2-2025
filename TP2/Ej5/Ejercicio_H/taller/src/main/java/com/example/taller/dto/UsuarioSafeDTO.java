package com.example.taller.dto;

import com.example.taller.entity.Rol;

/**
 * DTO seguro de Usuario para almacenar en sesión sin exponer la clave.
 */
public class UsuarioSafeDTO {
    private String id;
    private String nombreUsuario;
    private Rol rol;
    private Boolean eliminado;

    public UsuarioSafeDTO() {}

    public UsuarioSafeDTO(String id, String nombreUsuario, Rol rol, Boolean eliminado) {
        this.id = id;
        this.nombreUsuario = nombreUsuario;
        this.rol = rol;
        this.eliminado = eliminado;
    }

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

    public Rol getRol() {
        return rol;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }

    public Boolean getEliminado() {
        return eliminado;
    }

    public void setEliminado(Boolean eliminado) {
        this.eliminado = eliminado;
    }
}
