package com.example.etemplate.entities;

public class DetalleFront {
    private String articuloId;
    private String nombre;
    private double precio;
    private int cantidad;
    private Long imagenId; // <- agregar esto si querés mostrar imágenes

    public DetalleFront(String articulo, int cantidad,double precio) {
        this.articuloId =articulo;
        this.cantidad = cantidad;
        this.precio = precio;
    }


    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public String getArticuloId() {
        return articuloId;
    }

    public void setArticulo(String articuloId) {
        this.articuloId = articuloId;
    }

    public double getPrecio() {
        return this.precio;
    }
    public void setPrecio(int precio){
        this.precio = precio;
    }
    public void setImagenId(Long imgId){
        this.imagenId = imgId;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Long getImagenId() {
        return imagenId;
    }
}