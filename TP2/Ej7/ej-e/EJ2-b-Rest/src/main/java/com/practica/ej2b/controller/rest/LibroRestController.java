package com.practica.ej2b.controller.rest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.practica.ej2b.business.domain.entity.Libro;
import com.practica.ej2b.business.logic.service.LibroService;

@RestController
@RequestMapping("api/v1/libros")
public class LibroRestController extends BaseRestController<Libro, Long> {
    public LibroRestController(LibroService service) {
        super(service);
    }
}
