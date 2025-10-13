package com.practica.ej2consumer.controller.view;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.practica.ej2consumer.business.domain.dto.LocalidadDTO;
import com.practica.ej2consumer.business.logic.service.LocalidadService;

@Controller
@RequestMapping("/localidades")
public class LocalidadController extends BaseController<LocalidadDTO, Long> {
  
  public LocalidadController(LocalidadService service) {
    super(service, "localidades");
  }

  @Override
  protected LocalidadDTO createNewEntity() {
    return new LocalidadDTO();
  }

}
