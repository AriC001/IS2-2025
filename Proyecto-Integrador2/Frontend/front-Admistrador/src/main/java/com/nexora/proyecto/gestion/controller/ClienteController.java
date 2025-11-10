package com.nexora.proyecto.gestion.controller;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;

import com.nexora.proyecto.gestion.business.logic.service.ClienteService;
import com.nexora.proyecto.gestion.business.logic.service.LocalidadService;
import com.nexora.proyecto.gestion.business.logic.service.NacionalidadService;
import com.nexora.proyecto.gestion.business.logic.service.UsuarioService;
import com.nexora.proyecto.gestion.dto.ClienteDTO;
import com.nexora.proyecto.gestion.dto.DireccionDTO;

@Controller
@RequestMapping("/clientes")
public class ClienteController extends BaseController<ClienteDTO, String> {

  private final NacionalidadService nacionalidadService;
  private final LocalidadService localidadService;
  private final UsuarioService usuarioService;

  public ClienteController(ClienteService clienteService, NacionalidadService nacionalidadService, 
                          LocalidadService localidadService, UsuarioService usuarioService) {
    super(clienteService, "cliente", "clientes");
    this.nacionalidadService = nacionalidadService;
    this.localidadService = localidadService;
    this.usuarioService = usuarioService;
  }

  /**
   * Configura el formato de fecha para el binding de formularios.
   * El formato yyyy-MM-dd es el estándar para inputs de tipo "date" en HTML5.
   */
  @InitBinder
  public void initBinder(WebDataBinder binder) {
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    dateFormat.setLenient(false);
    binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
  }

  @Override
  protected ClienteDTO createNewEntity() {
    ClienteDTO cliente = new ClienteDTO();
    // Inicializar dirección para evitar NullPointerException
    if (cliente.getDireccion() == null) {
      cliente.setDireccion(new DireccionDTO());
    }
    return cliente;
  }

  @Override
  protected void prepareFormModel(Model model) {
    try {
      model.addAttribute("nacionalidades", nacionalidadService.findAllActives());
      model.addAttribute("localidades", localidadService.findAllActives());
      model.addAttribute("usuarios", usuarioService.findAllActives());
    } catch (Exception e) {
      logger.error("Error al preparar modelo del formulario: {}", e.getMessage());
    }
  }

}

