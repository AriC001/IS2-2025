package com.practica.ej2b.controller.rest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.practica.ej2b.business.domain.entity.Persona;
import com.practica.ej2b.business.logic.service.PersonaService;

@RestController
@RequestMapping("api/v1/personas")
public class PersonaRestController extends BaseRestController<Persona, Long> {
    public PersonaRestController(PersonaService service) {
        super(service);
    }
}
