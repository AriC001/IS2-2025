package com.is.biblioteca.business.logic.strategy;

import java.util.List;

import com.is.biblioteca.business.domain.entity.Libro;

/**
 * Patrón Strategy - Interfaz para las estrategias de búsqueda de libros
 */
public interface LibroBusquedaStrategy {
    
    /**
     * Método que define la estrategia de búsqueda
     * @param criterio El criterio de búsqueda (puede ser año, autor, editorial, etc.)
     * @return Lista de libros que coinciden con el criterio
     */
    List<Libro> buscar(String criterio);
    
    /**
     * Descripción de la estrategia
     * @return Nombre de la estrategia
     */
    String getNombreEstrategia();
}
