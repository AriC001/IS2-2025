package com.is.biblioteca.business.logic.strategy.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.is.biblioteca.business.domain.entity.Libro;
import com.is.biblioteca.business.logic.strategy.LibroBusquedaStrategy;
import com.is.biblioteca.business.persistence.repository.LibroRepository;

/**
 * Patrón Strategy - Estrategia concreta para búsqueda de libros por editorial
 */
@Component
public class BusquedaPorEditorialStrategy implements LibroBusquedaStrategy {

    @Autowired
    private LibroRepository libroRepository;

    @Override
    public List<Libro> buscar(String criterio) {
        if (criterio == null || criterio.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de la editorial no puede estar vacío");
        }
        return libroRepository.listarLibroPorEditorial(criterio);
    }

    @Override
    public String getNombreEstrategia() {
        return "Búsqueda por Editorial";
    }
}
