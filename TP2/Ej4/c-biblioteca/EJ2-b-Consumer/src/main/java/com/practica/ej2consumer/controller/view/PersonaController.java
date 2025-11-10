package com.practica.ej2consumer.controller.view;

import java.beans.PropertyEditorSupport;
import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.practica.ej2consumer.business.domain.dto.DomicilioDTO;
import com.practica.ej2consumer.business.domain.dto.PersonaDTO;
import com.practica.ej2consumer.business.logic.service.DomicilioService;
import com.practica.ej2consumer.business.logic.service.PersonaService;

@Controller
@RequestMapping("/personas")
public class PersonaController extends BaseController<PersonaDTO, Long> {
  
  private final DomicilioService domicilioService;

  public PersonaController(PersonaService service, DomicilioService domicilioService) {
    super(service, "personas");
    this.domicilioService = domicilioService;
  }

  @Override
  protected PersonaDTO createNewEntity() {
    return new PersonaDTO();
  }

  @ModelAttribute("domicilios")
  public List<DomicilioDTO> domiciliosDisponibles() {
    try {
      return domicilioService.findAllActives();
    } catch (Exception e) {
      return Collections.emptyList();
    }
  }

  @InitBinder
  public void initBinder(WebDataBinder binder) {
    binder.registerCustomEditor(DomicilioDTO.class, new PropertyEditorSupport() {
      @Override
      public void setAsText(String text) {
        if (text == null || text.isBlank()) {
          setValue(null);
        } else {
          DomicilioDTO domicilio = new DomicilioDTO();
          domicilio.setId(Long.valueOf(text));
          setValue(domicilio);
        }
      }
    });
  }

}
