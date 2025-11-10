package com.practica.ej2b.business.logic.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.practica.ej2b.business.domain.entity.Libro;
import com.practica.ej2b.business.strategy.LibroBusquedaStrategy;

import lombok.RequiredArgsConstructor;

/**
 * Servicio de búsqueda de libros usando el patrón Strategy.
 * Actúa como Contexto que selecciona la estrategia adecuada según el criterio.
 */
@Service
@RequiredArgsConstructor
public class LibroBusquedaService {

    // Spring inyecta automáticamente todas las implementaciones de LibroBusquedaStrategy
    // con la clave siendo el nombre del bean (@Component("busquedaPorTitulo"))
    private final Map<String, LibroBusquedaStrategy> estrategias;

    /**
     * Busca libros según el criterio y valor especificados.
     * 
     * @param criterio El tipo de búsqueda: "titulo", "autor", o "genero"
     * @param valor El valor a buscar
     * @return Lista de libros que coinciden con la búsqueda
     * @throws IllegalArgumentException si el criterio no es válido
     */
    public List<Libro> buscarLibros(String criterio, String valor) {
        // Construye el nombre del bean basándose en el criterio
        String beanName = "busquedaPor" + capitalize(criterio);
        
        LibroBusquedaStrategy estrategia = estrategias.get(beanName);
        
        if (estrategia == null) {
            throw new IllegalArgumentException(
                "Criterio de búsqueda no válido: " + criterio + 
                ". Los criterios permitidos son: titulo, autor, genero"
            );
        }
        
        return estrategia.buscar(valor);
    }

    /**
     * Capitaliza la primera letra de una cadena.
     */
    private String capitalize(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
    }

    /**
     * Obtiene los criterios de búsqueda disponibles.
     */
    public List<String> getCriteriosDisponibles() {
        return estrategias.values().stream()
                .map(LibroBusquedaStrategy::getCriterio)
                .toList();
    }
}
