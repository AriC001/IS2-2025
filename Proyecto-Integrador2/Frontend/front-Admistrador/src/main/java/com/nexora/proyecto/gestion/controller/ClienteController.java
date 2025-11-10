package com.nexora.proyecto.gestion.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.nexora.proyecto.gestion.business.logic.service.ClienteService;
import com.nexora.proyecto.gestion.business.logic.service.NacionalidadService;
import com.nexora.proyecto.gestion.business.logic.service.LocalidadService;
import com.nexora.proyecto.gestion.dto.ClienteDTO;

@Controller
@RequestMapping("/clientes")
public class ClienteController extends BaseController<ClienteDTO, String> {

  private final NacionalidadService nacionalidadService;
  private final LocalidadService localidadService;

  public ClienteController(ClienteService clienteService, NacionalidadService nacionalidadService, LocalidadService localidadService) {
    super(clienteService, "cliente", "clientes");
    this.nacionalidadService = nacionalidadService;
    this.localidadService = localidadService;
  }

  @Override
  protected ClienteDTO createNewEntity() {
    return new ClienteDTO();
  }

  @Override
  protected void prepareFormModel(Model model) {
    try {
      model.addAttribute("nacionalidades", nacionalidadService.findAllActives());
      model.addAttribute("localidades", localidadService.findAllActives());
    } catch (Exception e) {
      logger.error("Error al preparar modelo del formulario: {}", e.getMessage());
    }
  }

}

