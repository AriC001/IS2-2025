package com.practica.ej2b.controller.rest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.practica.ej2b.business.domain.dto.PersonaDTO;
import com.practica.ej2b.business.domain.entity.Persona;
import com.practica.ej2b.business.logic.adapter.impl.PersonaAdapter;
import com.practica.ej2b.business.logic.service.PersonaService;

@RestController
@RequestMapping("api/v1/personas")
public class PersonaRestController extends BaseRestController<Persona, PersonaDTO, Long> {
    
    public PersonaRestController(PersonaService service, PersonaAdapter adapter) {
        super(service, adapter);
    }
}
