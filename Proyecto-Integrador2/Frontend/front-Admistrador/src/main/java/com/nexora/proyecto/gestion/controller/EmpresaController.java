package com.nexora.proyecto.gestion.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.nexora.proyecto.gestion.business.logic.service.EmpresaService;
import com.nexora.proyecto.gestion.business.logic.service.LocalidadService;
import com.nexora.proyecto.gestion.dto.DireccionDTO;
import com.nexora.proyecto.gestion.dto.EmpresaDTO;

@Controller
@RequestMapping("/empresas")
public class EmpresaController extends BaseController<EmpresaDTO, String> {

  private final LocalidadService localidadService;

  public EmpresaController(EmpresaService empresaService, LocalidadService localidadService) {
    super(empresaService, "empresa", "empresas");
    this.localidadService = localidadService;
  }

  @Override
  protected EmpresaDTO createNewEntity() {
    EmpresaDTO empresa = new EmpresaDTO();
    // Inicializar dirección para evitar NullPointerException
    if (empresa.getDireccion() == null) {
      empresa.setDireccion(new DireccionDTO());
    }
    return empresa;
  }

  @Override
  protected void prepareFormModel(Model model) {
    try {
      model.addAttribute("localidades", localidadService.findAllActives());
    } catch (Exception e) {
      logger.error("Error al preparar modelo del formulario: {}", e.getMessage());
    }
  }

}

