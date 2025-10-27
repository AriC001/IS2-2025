package com.practica.ej2b.business.strategy.impl;

import java.util.List;

import org.springframework.stereotype.Component;

import com.practica.ej2b.business.domain.entity.Libro;
import com.practica.ej2b.business.persistence.repository.LibroRepository;
import com.practica.ej2b.business.strategy.LibroBusquedaStrategy;

import lombok.RequiredArgsConstructor;

/**
 * Estrategia de búsqueda por género.
 * Busca libros cuyo género contenga el valor especificado (case-insensitive).
 */
@Component("busquedaPorGenero")
@RequiredArgsConstructor
public class BusquedaPorGeneroStrategy implements LibroBusquedaStrategy {

    private final LibroRepository libroRepository;

    @Override
    public List<Libro> buscar(String valor) {
        return libroRepository.findAllByGenero(valor);
    }

    @Override
    public String getCriterio() {
        return "genero";
    }
}
