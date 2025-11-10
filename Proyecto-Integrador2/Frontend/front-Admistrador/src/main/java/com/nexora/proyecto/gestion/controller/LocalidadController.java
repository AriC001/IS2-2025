package com.nexora.proyecto.gestion.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.nexora.proyecto.gestion.business.logic.service.DepartamentoService;
import com.nexora.proyecto.gestion.business.logic.service.LocalidadService;
import com.nexora.proyecto.gestion.dto.LocalidadDTO;

@Controller
@RequestMapping("/localidades")
public class LocalidadController extends BaseController<LocalidadDTO, String> {

  private final DepartamentoService departamentoService;

  public LocalidadController(LocalidadService localidadService, DepartamentoService departamentoService) {
    super(localidadService, "localidad", "localidades");
    this.departamentoService = departamentoService;
  }

  @Override
  protected LocalidadDTO createNewEntity() {
    return new LocalidadDTO();
  }

  @Override
  protected void prepareFormModel(Model model) {
    try {
      model.addAttribute("departamentos", departamentoService.findAllActives());
    } catch (Exception e) {
      logger.error("Error al preparar modelo del formulario: {}", e.getMessage());
    }
  }
}

