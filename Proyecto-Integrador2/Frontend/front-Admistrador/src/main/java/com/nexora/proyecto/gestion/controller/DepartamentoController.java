package com.nexora.proyecto.gestion.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.nexora.proyecto.gestion.business.logic.service.DepartamentoService;
import com.nexora.proyecto.gestion.business.logic.service.ProvinciaService;
import com.nexora.proyecto.gestion.dto.DepartamentoDTO;

@Controller
@RequestMapping("/departamentos")
public class DepartamentoController extends BaseController<DepartamentoDTO, String> {

  private final ProvinciaService provinciaService;

  public DepartamentoController(DepartamentoService departamentoService, ProvinciaService provinciaService) {
    super(departamentoService, "departamento", "departamentos");
    this.provinciaService = provinciaService;
  }

  @Override
  protected DepartamentoDTO createNewEntity() {
    return new DepartamentoDTO();
  }

  @Override
  protected void prepareFormModel(Model model) {
    try {
      model.addAttribute("provincias", provinciaService.findAllActives());
    } catch (Exception e) {
      logger.error("Error al preparar modelo del formulario: {}", e.getMessage());
    }
  }
}

