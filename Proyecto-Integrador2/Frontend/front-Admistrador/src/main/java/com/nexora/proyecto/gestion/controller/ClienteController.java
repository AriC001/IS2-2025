package com.nexora.proyecto.gestion.controller;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.nexora.proyecto.gestion.business.logic.service.ClienteService;
import com.nexora.proyecto.gestion.business.logic.service.DireccionService;
import com.nexora.proyecto.gestion.business.logic.service.LocalidadService;
import com.nexora.proyecto.gestion.business.logic.service.NacionalidadService;
import com.nexora.proyecto.gestion.business.logic.service.UsuarioService;
import com.nexora.proyecto.gestion.dto.ClienteDTO;
import com.nexora.proyecto.gestion.dto.DireccionDTO;
import com.nexora.proyecto.gestion.dto.LocalidadDTO;
import com.nexora.proyecto.gestion.dto.NacionalidadDTO;
import com.nexora.proyecto.gestion.dto.UsuarioDTO;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/clientes")
public class ClienteController extends BaseController<ClienteDTO, String> {

  private final NacionalidadService nacionalidadService;
  private final LocalidadService localidadService;
  private final UsuarioService usuarioService;
  private final DireccionService direccionService;

  public ClienteController(ClienteService clienteService, NacionalidadService nacionalidadService,
      LocalidadService localidadService, UsuarioService usuarioService, DireccionService direccionService) {
    super(clienteService, "cliente", "clientes");
    this.nacionalidadService = nacionalidadService;
    this.localidadService = localidadService;
    this.usuarioService = usuarioService;
    this.direccionService = direccionService;
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
    if (cliente.getDireccion() == null) {
      DireccionDTO direccion = new DireccionDTO();
      direccion.setLocalidad(new LocalidadDTO());
      cliente.setDireccion(direccion);
    } else if (cliente.getDireccion().getLocalidad() == null) {
      cliente.getDireccion().setLocalidad(new LocalidadDTO());
    }
    if (cliente.getNacionalidad() == null) {
      cliente.setNacionalidad(new NacionalidadDTO());
    }
    if (cliente.getUsuario() == null) {
      cliente.setUsuario(new UsuarioDTO());
    }
    return cliente;
  }

  @Override
  protected void prepareFormModel(Model model) {
    try {
      ClienteDTO cliente = (ClienteDTO) model.getAttribute("cliente");
      if (cliente != null) {
        if (cliente.getUsuario() == null) {
          cliente.setUsuario(new UsuarioDTO());
        }
        if (cliente.getNacionalidad() == null) {
          cliente.setNacionalidad(new NacionalidadDTO());
        }
        if (cliente.getDireccion() == null) {
          DireccionDTO direccion = new DireccionDTO();
          direccion.setLocalidad(new LocalidadDTO());
          cliente.setDireccion(direccion);
        } else if (cliente.getDireccion().getLocalidad() == null) {
          cliente.getDireccion().setLocalidad(new LocalidadDTO());
        }
      }
      model.addAttribute("nacionalidades", nacionalidadService.findAllActives());
      model.addAttribute("localidades", localidadService.findAllActives());
      model.addAttribute("usuarios", usuarioService.findAllActives());
      model.addAttribute("direcciones", direccionService.findAllActives());
    } catch (Exception e) {
      logger.error("Error al preparar modelo del formulario: {}", e.getMessage());
    }
  }

  @Override
  @PostMapping
  public String crear(ClienteDTO entity, RedirectAttributes redirectAttributes, HttpSession session) {
    normalizeCliente(entity);
    return super.crear(entity, redirectAttributes, session);
  }

  @Override
  @PostMapping("/{id}")
  public String actualizar(@PathVariable String id, ClienteDTO entity, RedirectAttributes redirectAttributes,
      HttpSession session) {
    normalizeCliente(entity);
    return super.actualizar(id, entity, redirectAttributes, session);
  }

  private void normalizeCliente(ClienteDTO entity) {
    if (entity.getDireccion() != null && !StringUtils.hasText(entity.getDireccion().getId())) {
      entity.setDireccion(null);
    }
    if (entity.getUsuario() != null && !StringUtils.hasText(entity.getUsuario().getId())) {
      entity.setUsuario(null);
    }
    if (entity.getNacionalidad() != null && !StringUtils.hasText(entity.getNacionalidad().getId())) {
      entity.setNacionalidad(null);
    }
  }
}

