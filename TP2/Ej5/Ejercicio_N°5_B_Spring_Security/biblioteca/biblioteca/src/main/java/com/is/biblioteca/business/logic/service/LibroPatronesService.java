package com.is.biblioteca.business.logic.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.is.biblioteca.business.domain.entity.Libro;
import com.is.biblioteca.business.logic.strategy.LibroBusquedaContext;
import com.is.biblioteca.business.logic.strategy.impl.BusquedaPorAnioStrategy;
import com.is.biblioteca.business.logic.strategy.impl.BusquedaPorAutorStrategy;
import com.is.biblioteca.business.logic.strategy.impl.BusquedaPorEditorialStrategy;

/**
 * Servicio que demuestra el uso de los patrones Strategy y Facade
 * para búsquedas y registro de libros
 */
@Service
public class LibroPatronesService {

    @Autowired
    private LibroBusquedaContext busquedaContext;

    @Autowired
    private BusquedaPorAnioStrategy busquedaPorAnioStrategy;

    @Autowired
    private BusquedaPorAutorStrategy busquedaPorAutorStrategy;

    @Autowired
    private BusquedaPorEditorialStrategy busquedaPorEditorialStrategy;

    /**
     * Patrón Strategy: Busca libros por año
     * @param anio Año de publicación a buscar
     * @return Lista de libros publicados en ese año
     */
    public List<Libro> buscarLibrosPorAnio(String anio) {
        busquedaContext.setStrategy(busquedaPorAnioStrategy);
        return busquedaContext.ejecutarBusqueda(anio);
    }

    /**
     * Patrón Strategy: Busca libros por autor
     * @param nombreAutor Nombre del autor a buscar
     * @return Lista de libros del autor
     */
    public List<Libro> buscarLibrosPorAutor(String nombreAutor) {
        busquedaContext.setStrategy(busquedaPorAutorStrategy);
        return busquedaContext.ejecutarBusqueda(nombreAutor);
    }

    /**
     * Patrón Strategy: Busca libros por editorial
     * @param nombreEditorial Nombre de la editorial a buscar
     * @return Lista de libros de la editorial
     */
    public List<Libro> buscarLibrosPorEditorial(String nombreEditorial) {
        busquedaContext.setStrategy(busquedaPorEditorialStrategy);
        return busquedaContext.ejecutarBusqueda(nombreEditorial);
    }

}
