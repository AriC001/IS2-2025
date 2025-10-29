package com.example.medicine.services;

import com.example.medicine.errors.ErrorServicio;

public abstract class EntityServiceTemplate<T> {

    // --- Métodos plantilla (template methods) ---
    public final void crear(T entidad) throws ErrorServicio {
        validar(entidad);
        guardar(entidad);
        notificar("creado");
    }

    public final void modificar(T entidad) throws ErrorServicio {
        validar(entidad);
        actualizar(entidad);
        notificar("actualizado");
    }

    public final void borrar(T entidad) throws ErrorServicio {
        eliminar(entidad);
        notificar("eliminado");
    }

    // --- Métodos que las subclases deben implementar ---
    protected abstract void validar(T entidad) throws ErrorServicio;
    protected abstract void guardar(T entidad) throws ErrorServicio;
   public abstract void actualizar(T entidad) throws ErrorServicio;
   public abstract void eliminar(T entidad) throws ErrorServicio;


    protected void notificar(String accion) {
        System.out.println("Entidad " + accion + " correctamente.");
    }
}
