package com.is.biblioteca.business.logic.strategy;

import java.util.List;

import org.springframework.stereotype.Component;

import com.is.biblioteca.business.domain.entity.Libro;

/**
 * Patrón Strategy - Contexto que utiliza las estrategias de búsqueda
 */
@Component
public class LibroBusquedaContext {

    private LibroBusquedaStrategy strategy;

    /**
     * Establece la estrategia de búsqueda a utilizar
     */
    public void setStrategy(LibroBusquedaStrategy strategy) {
        this.strategy = strategy;
    }

    /**
     * Ejecuta la búsqueda utilizando la estrategia configurada
     * @param criterio Criterio de búsqueda (año, nombre de autor o editorial)
     * @return Lista de libros que coinciden con el criterio
     * @throws IllegalStateException si no se ha configurado ninguna estrategia
     */
    public List<Libro> ejecutarBusqueda(String criterio) {
        if (strategy == null) {
            throw new IllegalStateException("No se ha configurado ninguna estrategia de búsqueda");
        }
        return strategy.buscar(criterio);
    }

    /**
     * Obtiene el nombre de la estrategia actual
     * @return Nombre de la estrategia configurada
     */
    public String getNombreEstrategiaActual() {
        if (strategy == null) {
            return "Ninguna estrategia configurada";
        }
        return strategy.getNombreEstrategia();
    }
}
