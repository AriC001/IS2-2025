package com.practica.ej2b.controller.rest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.practica.ej2b.business.domain.dto.LocalidadDTO;
import com.practica.ej2b.business.domain.entity.Localidad;
import com.practica.ej2b.business.logic.adapter.impl.LocalidadAdapter;
import com.practica.ej2b.business.logic.service.LocalidadService;

@RestController
@RequestMapping("api/v1/localidades")
public class LocalidadRestController extends BaseRestController<Localidad, LocalidadDTO, Long> {
    
    public LocalidadRestController(LocalidadService service, LocalidadAdapter adapter) {
        super(service, adapter);
    }
}
