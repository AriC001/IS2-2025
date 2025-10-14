package com.example.etemplate.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Direccion {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private Double latitud;   // Latitud de la dirección
    private Double longitud;  // Longitud de la dirección

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public Direccion(Double latitud, Double longitud) {
        this.latitud = latitud;
        this.longitud = longitud;
    }
    public Direccion(){}

    public Double getLatitud() {
        return latitud;
    }

    public void setLatitud(Double latitud) {
        this.latitud = latitud;
    }

    public Double getLongitud() {
        return longitud;
    }

    public void setLongitud(Double longitud) {
        this.longitud = longitud;
    }

    @Override
    public String toString() {
        if (latitud == null || longitud == null) return "Sin coordenadas";
        return latitud + ", " + longitud;
    }
}
