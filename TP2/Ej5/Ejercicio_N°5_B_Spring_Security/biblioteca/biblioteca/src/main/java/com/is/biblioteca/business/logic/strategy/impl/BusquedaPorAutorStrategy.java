package com.is.biblioteca.business.logic.strategy.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.is.biblioteca.business.domain.entity.Autor;
import com.is.biblioteca.business.domain.entity.Libro;
import com.is.biblioteca.business.logic.strategy.LibroBusquedaStrategy;
import com.is.biblioteca.business.persistence.repository.AutorRepository;
import com.is.biblioteca.business.persistence.repository.LibroRepository;

/**
 * Patrón Strategy - Estrategia concreta para búsqueda de libros por autor
 */
@Component
public class BusquedaPorAutorStrategy implements LibroBusquedaStrategy {

    @Autowired
    private LibroRepository libroRepository;

    @Autowired
    private AutorRepository autorRepository;

    @Override
    public List<Libro> buscar(String criterio) {
        if (criterio == null || criterio.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del autor no puede estar vacío");
        }

        // Buscar el autor por nombre
        Autor autor = autorRepository.buscarAutorPorNombre(criterio);
        
        if (autor == null) {
            // Si no se encuentra el autor, retornar lista vacía
            return new ArrayList<>();
        }

        // Buscar libros por el ID del autor
        return libroRepository.listarLibroPorAutor(autor.getId());
    }

    @Override
    public String getNombreEstrategia() {
        return "Búsqueda por Autor";
    }
}
