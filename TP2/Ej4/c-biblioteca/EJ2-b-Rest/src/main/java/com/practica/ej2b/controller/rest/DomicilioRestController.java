package com.practica.ej2b.controller.rest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.practica.ej2b.business.domain.dto.DomicilioDTO;
import com.practica.ej2b.business.domain.entity.Domicilio;
import com.practica.ej2b.business.logic.adapter.impl.DomicilioAdapter;
import com.practica.ej2b.business.logic.service.DomicilioService;

@RestController
@RequestMapping("api/v1/domicilios")
public class DomicilioRestController extends BaseRestController<Domicilio, DomicilioDTO, Long> {
    
    public DomicilioRestController(DomicilioService service, DomicilioAdapter adapter) {
        super(service, adapter);
    }
}
