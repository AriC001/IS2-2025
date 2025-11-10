package com.practica.ej2b.business.strategy.impl;

import java.util.List;

import org.springframework.stereotype.Component;

import com.practica.ej2b.business.domain.entity.Libro;
import com.practica.ej2b.business.persistence.repository.LibroRepository;
import com.practica.ej2b.business.strategy.LibroBusquedaStrategy;

import lombok.RequiredArgsConstructor;

/**
 * Estrategia de búsqueda por título.
 * Busca libros cuyo título contenga el valor especificado (case-insensitive).
 */
@Component("busquedaPorTitulo")
@RequiredArgsConstructor
public class BusquedaPorTituloStrategy implements LibroBusquedaStrategy {

    private final LibroRepository libroRepository;

    @Override
    public List<Libro> buscar(String valor) {
        // Busca en todos los libros activos (no eliminados)
        return libroRepository.findAllByTitulo(valor);
    }

    @Override
    public String getCriterio() {
        return "titulo";
    }
}
