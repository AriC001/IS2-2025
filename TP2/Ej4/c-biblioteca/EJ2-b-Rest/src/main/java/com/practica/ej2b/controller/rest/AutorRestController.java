package com.practica.ej2b.controller.rest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.practica.ej2b.business.domain.dto.AutorDTO;
import com.practica.ej2b.business.domain.entity.Autor;
import com.practica.ej2b.business.logic.adapter.impl.AutorAdapter;
import com.practica.ej2b.business.logic.service.AutorService;

@RestController
@RequestMapping("api/v1/autores")
public class AutorRestController extends BaseRestController<Autor, AutorDTO, Long> {
    
    public AutorRestController(AutorService service, AutorAdapter adapter) {
        super(service, adapter);
    }
}
