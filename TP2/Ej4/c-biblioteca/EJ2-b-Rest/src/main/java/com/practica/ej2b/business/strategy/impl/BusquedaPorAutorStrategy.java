package com.practica.ej2b.business.strategy.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.practica.ej2b.business.domain.entity.Autor;
import com.practica.ej2b.business.domain.entity.Libro;
import com.practica.ej2b.business.persistence.repository.LibroRepository;
import com.practica.ej2b.business.strategy.LibroBusquedaStrategy;

import lombok.RequiredArgsConstructor;

/**
 * Estrategia de búsqueda por autor.
 * Busca libros cuyos autores contengan el valor en su nombre o apellido (case-insensitive).
 */
@Component("busquedaPorAutor")
@RequiredArgsConstructor
public class BusquedaPorAutorStrategy implements LibroBusquedaStrategy {

    private final LibroRepository libroRepository;

    @Override
    public List<Libro> buscar(String valor) {
        return libroRepository.findAllByEliminadoFalse().stream()
                .filter(libro -> libro.getAutores() != null && 
                        libro.getAutores().stream()
                            .anyMatch(autor -> autorCoincide(autor, valor)))
                .collect(Collectors.toList());
    }

    private boolean autorCoincide(Autor autor, String valor) {
        String valorLower = valor.toLowerCase();
        String nombreCompleto = (autor.getNombre() + " " + autor.getApellido()).toLowerCase();
        return nombreCompleto.contains(valorLower) ||
               (autor.getNombre() != null && autor.getNombre().toLowerCase().contains(valorLower)) ||
               (autor.getApellido() != null && autor.getApellido().toLowerCase().contains(valorLower));
    }

    @Override
    public String getCriterio() {
        return "autor";
    }
}
