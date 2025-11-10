package com.example.etemplate.entities;

import jakarta.persistence.*;
import org.springframework.data.relational.core.mapping.Table;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "carrito_back")
public class CarritoBack extends CarritoTemplate{
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne
    private Usuario usuario;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "carrito_id")
    private List<Detalle> detalles;


    public CarritoBack() {
        super();
    }

    @Override
    protected void seleccionarProductos() {

    }
    public String getShortId(){
        int length = this.id.length();
        return this.id.substring(length-5,length);
    }

    @Override
    protected void calcularTotal() {

    }

    @Override
    protected void procesarPago() {

    }

    @Override
    protected void confirmarCompra() {

    }

    public CarritoBack finalizarCompra() {
        CarritoBack back = new CarritoBack();
        back.setDetalles(new ArrayList<>(this.detalles));
        back.setTotal(this.total);
        back.setUsuario(this.usuario);
        return back;
    }

    public void setDetalles(List<Detalle> detalles) {
        this.detalles = detalles;
    }

    public void setUsuario(Usuario usuario){
        this.usuario = usuario;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public List<Detalle> getDetalles() {
        return detalles;
    }

    @Override
    public void setDeleted(boolean deleted) { this.deleted = deleted; }
    @Override
    public boolean isDeleted() { return deleted; }
}
