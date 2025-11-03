package com.practica.ej2b.controller.rest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.practica.ej2b.business.domain.entity.Domicilio;
import com.practica.ej2b.business.logic.service.DomicilioService;

@RestController
@RequestMapping("api/v1/domicilios")
public class DomicilioRestController extends BaseRestController<Domicilio, Long> {
    public DomicilioRestController(DomicilioService service) {
        super(service);
    }
}
