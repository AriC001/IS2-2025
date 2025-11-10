package com.nexora.proyecto.gestion.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.nexora.proyecto.gestion.business.logic.service.DireccionService;
import com.nexora.proyecto.gestion.business.logic.service.LocalidadService;
import com.nexora.proyecto.gestion.dto.DireccionDTO;
import com.nexora.proyecto.gestion.dto.LocalidadDTO;

@Controller
@RequestMapping("/direcciones")
public class DireccionController extends BaseController<DireccionDTO, String> {

  private final LocalidadService localidadService;

  public DireccionController(DireccionService direccionService, LocalidadService localidadService) {
    super(direccionService, "direccion", "direcciones");
    this.localidadService = localidadService;
  }

  @Override
  protected DireccionDTO createNewEntity() {
    DireccionDTO direccion = new DireccionDTO();
    // Inicializar localidad para evitar NullPointerException
    if (direccion.getLocalidad() == null) {
      direccion.setLocalidad(new LocalidadDTO());
    }
    return direccion;
  }

  @Override
  protected void prepareFormModel(Model model) {
    try {
      model.addAttribute("localidades", localidadService.findAllActives());
    } catch (Exception e) {
      logger.error("Error al preparar modelo del formulario: {}", e.getMessage(), e);
      model.addAttribute("error", "Error al cargar localidades: " + e.getMessage());
      model.addAttribute("localidades", java.util.Collections.emptyList());
    }
  }
}

