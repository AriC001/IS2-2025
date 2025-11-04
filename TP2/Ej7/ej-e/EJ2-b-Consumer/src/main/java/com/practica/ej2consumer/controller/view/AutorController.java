package com.practica.ej2consumer.controller.view;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.practica.ej2consumer.business.domain.dto.AutorDTO;
import com.practica.ej2consumer.business.logic.service.AutorService;

@Controller
@RequestMapping("/autores")
public class AutorController extends BaseController<AutorDTO, Long> {
  
  public AutorController(AutorService service) {
    super(service, "autores");
  }

  @Override
  protected AutorDTO createNewEntity() {
    return new AutorDTO();
  }

}
