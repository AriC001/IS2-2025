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
import com.practica.ej2consumer.business.domain.dto.LocalidadDTO;
import com.practica.ej2consumer.business.logic.service.DomicilioService;
import com.practica.ej2consumer.business.logic.service.LocalidadService;

@Controller
@RequestMapping("/domicilios")
public class DomicilioController extends BaseController<DomicilioDTO, Long> {
  
  private final LocalidadService localidadService;

  public DomicilioController(DomicilioService service, LocalidadService localidadService) {
    super(service, "domicilios");
    this.localidadService = localidadService;
  }

  @Override
  protected DomicilioDTO createNewEntity() {
    return new DomicilioDTO();
  }

  @ModelAttribute("localidadesDisponibles")
  public List<LocalidadDTO> localidadesDisponibles() {
    try {
      return localidadService.findAllActives();
    } catch (Exception e) {
      return Collections.emptyList();
    }
  }

  @InitBinder
  public void initBinder(WebDataBinder binder) {
    binder.registerCustomEditor(LocalidadDTO.class, new PropertyEditorSupport() {
      @Override
      public void setAsText(String text) {
        if (text == null || text.isBlank()) {
          setValue(null);
        } else {
          LocalidadDTO localidad = new LocalidadDTO();
          localidad.setId(Long.valueOf(text));
          setValue(localidad);
        }
      }
    });
  }

}
