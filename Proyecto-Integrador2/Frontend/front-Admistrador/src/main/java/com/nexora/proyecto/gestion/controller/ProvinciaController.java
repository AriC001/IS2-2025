package com.nexora.proyecto.gestion.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.nexora.proyecto.gestion.business.logic.service.PaisService;
import com.nexora.proyecto.gestion.business.logic.service.ProvinciaService;
import com.nexora.proyecto.gestion.dto.ProvinciaDTO;

@Controller
@RequestMapping("/provincias")
public class ProvinciaController extends BaseController<ProvinciaDTO, String> {

  private final PaisService paisService;

  public ProvinciaController(ProvinciaService provinciaService, PaisService paisService) {
    super(provinciaService, "provincia", "provincias");
    this.paisService = paisService;
  }

  @Override
  protected ProvinciaDTO createNewEntity() {
    return new ProvinciaDTO();
  }

  @Override
  protected void prepareFormModel(Model model) {
    try {
      model.addAttribute("paises", paisService.findAllActives());
    } catch (Exception e) {
      logger.error("Error al preparar modelo del formulario: {}", e.getMessage());
    }
  }
}

