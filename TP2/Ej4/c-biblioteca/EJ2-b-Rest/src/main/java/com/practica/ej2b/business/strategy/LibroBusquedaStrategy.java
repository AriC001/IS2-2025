package com.practica.ej2b.business.strategy;

import java.util.List;

import com.practica.ej2b.business.domain.entity.Libro;

/**
 * Interfaz Strategy para diferentes algoritmos de búsqueda de libros.
 * Implementa el patrón Strategy para permitir intercambiar algoritmos de búsqueda en runtime.
 */
public interface LibroBusquedaStrategy {
    
    /**
     * Busca libros según el criterio de la estrategia.
     * 
     * @param valor valor a buscar
     * @return lista de libros que coinciden
     */
    List<Libro> buscar(String valor);
    
    /**
     * Obtiene el nombre del criterio de búsqueda.
     * 
     * @return nombre del criterio (ej: "titulo", "autor", "genero")
     */
    String getCriterio();
}
