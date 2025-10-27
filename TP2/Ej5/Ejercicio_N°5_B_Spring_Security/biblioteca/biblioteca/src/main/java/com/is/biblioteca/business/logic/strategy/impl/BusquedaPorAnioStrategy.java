package com.is.biblioteca.business.logic.strategy.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.is.biblioteca.business.domain.entity.Libro;
import com.is.biblioteca.business.logic.strategy.LibroBusquedaStrategy;
import com.is.biblioteca.business.persistence.repository.LibroRepository;

/**
 * Patrón Strategy - Estrategia concreta para búsqueda de libros por año
 */
@Component
public class BusquedaPorAnioStrategy implements LibroBusquedaStrategy {

    @Autowired
    private LibroRepository libroRepository;

    @Override
    public List<Libro> buscar(String criterio) {
        try {
            Integer anio = Integer.valueOf(criterio);
            return libroRepository.findAllByAnio(anio);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("El criterio debe ser un año válido (número entero)");
        }
    }

    @Override
    public String getNombreEstrategia() {
        return "Búsqueda por Año";
    }
}
