package com.nexora.proyecto.gestion.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.nexora.proyecto.gestion.business.logic.service.PaisService;
import com.nexora.proyecto.gestion.dto.PaisDTO;

@Controller
@RequestMapping("/paises")
public class PaisController extends BaseController<PaisDTO, String> {

  public PaisController(PaisService paisService) {
    super(paisService, "pais", "paises");
  }

  @Override
  protected PaisDTO createNewEntity() {
    return new PaisDTO();
  }

  @Override
  protected void prepareFormModel(Model model) {
    // No necesita datos adicionales
  }
}

