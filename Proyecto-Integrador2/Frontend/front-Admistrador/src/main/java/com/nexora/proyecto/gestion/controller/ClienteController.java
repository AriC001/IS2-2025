package com.nexora.proyecto.gestion.controller;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.nexora.proyecto.gestion.business.logic.service.ClienteService;
import com.nexora.proyecto.gestion.business.logic.service.ContactoCorreoElectronicoService;
import com.nexora.proyecto.gestion.business.logic.service.ContactoTelefonicoService;
import com.nexora.proyecto.gestion.business.logic.service.DireccionService;
import com.nexora.proyecto.gestion.business.logic.service.LocalidadService;
import com.nexora.proyecto.gestion.business.logic.service.NacionalidadService;
import com.nexora.proyecto.gestion.business.logic.service.UsuarioService;
import com.nexora.proyecto.gestion.dto.ClienteDTO;
import com.nexora.proyecto.gestion.dto.ContactoCorreoElectronicoDTO;
import com.nexora.proyecto.gestion.dto.ContactoTelefonicoDTO;
import com.nexora.proyecto.gestion.dto.DireccionDTO;
import com.nexora.proyecto.gestion.dto.LocalidadDTO;
import com.nexora.proyecto.gestion.dto.NacionalidadDTO;
import com.nexora.proyecto.gestion.dto.UsuarioDTO;
import com.nexora.proyecto.gestion.dto.enums.TipoContacto;
import com.nexora.proyecto.gestion.dto.enums.TipoTelefono;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/clientes")
public class ClienteController extends BaseController<ClienteDTO, String> {

  private final NacionalidadService nacionalidadService;
  private final LocalidadService localidadService;
  private final UsuarioService usuarioService;
  private final DireccionService direccionService;
  private final ContactoTelefonicoService contactoTelefonicoService;
  private final ContactoCorreoElectronicoService contactoCorreoElectronicoService;

  public ClienteController(ClienteService clienteService, NacionalidadService nacionalidadService,
      LocalidadService localidadService, UsuarioService usuarioService, DireccionService direccionService,
      ContactoTelefonicoService contactoTelefonicoService, ContactoCorreoElectronicoService contactoCorreoElectronicoService) {
    super(clienteService, "cliente", "clientes");
    this.nacionalidadService = nacionalidadService;
    this.localidadService = localidadService;
    this.usuarioService = usuarioService;
    this.direccionService = direccionService;
    this.contactoTelefonicoService = contactoTelefonicoService;
    this.contactoCorreoElectronicoService = contactoCorreoElectronicoService;
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
    } catch (Exception e) {
      logger.error("Error al inicializar cliente en el modelo: {}", e.getMessage(), e);
    }

    // Cargar direcciones con manejo de errores específico
    try {
      List<DireccionDTO> direcciones = direccionService.findAllActives();
      logger.debug("Cargadas {} direcciones para el formulario de cliente", direcciones != null ? direcciones.size() : 0);
      model.addAttribute("direcciones", direcciones != null ? direcciones : Collections.emptyList());
    } catch (Exception e) {
      logger.error("Error al cargar direcciones para el formulario de cliente: {}", e.getMessage(), e);
      logger.error("Stack trace completo:", e);
      // Asegurar que siempre haya un atributo direcciones en el modelo, incluso si está vacío
      model.addAttribute("direcciones", Collections.emptyList());
    }

    // Cargar localidades con manejo de errores específico
    try {
      List<LocalidadDTO> localidades = localidadService.findAllActives();
      logger.debug("Cargadas {} localidades para el formulario de cliente", localidades != null ? localidades.size() : 0);
      model.addAttribute("localidades", localidades != null ? localidades : Collections.emptyList());
    } catch (Exception e) {
      logger.error("Error al cargar localidades para el formulario de cliente: {}", e.getMessage(), e);
      model.addAttribute("localidades", Collections.emptyList());
    }

    // Cargar nacionalidades con manejo de errores específico
    try {
      List<NacionalidadDTO> nacionalidades = nacionalidadService.findAllActives();
      logger.debug("Cargadas {} nacionalidades para el formulario de cliente", nacionalidades != null ? nacionalidades.size() : 0);
      model.addAttribute("nacionalidades", nacionalidades != null ? nacionalidades : Collections.emptyList());
    } catch (Exception e) {
      logger.error("Error al cargar nacionalidades para el formulario de cliente: {}", e.getMessage(), e);
      model.addAttribute("nacionalidades", Collections.emptyList());
    }

    // Cargar usuarios con manejo de errores específico
    try {
      List<UsuarioDTO> usuarios = usuarioService.findAllActives();
      logger.debug("Cargados {} usuarios para el formulario de cliente", usuarios != null ? usuarios.size() : 0);
      model.addAttribute("usuarios", usuarios != null ? usuarios : Collections.emptyList());
    } catch (Exception e) {
      logger.error("Error al cargar usuarios para el formulario de cliente: {}", e.getMessage(), e);
      model.addAttribute("usuarios", Collections.emptyList());
    }

    // Agregar tipos de teléfono para el formulario
    model.addAttribute("tiposTelefono", TipoTelefono.values());
  }

  @PostMapping("/crear-con-contactos")
  public String crearConContactos(ClienteDTO entity, @RequestParam(required = false) String telefono,
      @RequestParam(required = false) String tipoTelefono, @RequestParam(required = false) String email, 
      RedirectAttributes redirectAttributes, HttpSession session) {
    String redirect = checkSession(session);
    if (redirect != null) {
      return redirect;
    }
    try {
      normalizeCliente(entity);
      ClienteDTO clienteCreado = service.create(entity);
      // Crear los contactos básicos si se proporcionaron
      crearContactosBasicos(clienteCreado, telefono, tipoTelefono, email);
      addSuccessMessage(redirectAttributes, "Cliente creado exitosamente");
      return "redirect:/clientes";
    } catch (Exception e) {
      handleException(e, redirectAttributes, "crear");
      redirectAttributes.addFlashAttribute("cliente", entity);
      return "redirect:/clientes/nuevo";
    }
  }

  @PostMapping("/{id}/actualizar-con-contactos")
  public String actualizarConContactos(@PathVariable String id, ClienteDTO entity, @RequestParam(required = false) String telefono,
      @RequestParam(required = false) String tipoTelefono, @RequestParam(required = false) String email, 
      RedirectAttributes redirectAttributes, HttpSession session) {
    String redirect = checkSession(session);
    if (redirect != null) {
      return redirect;
    }
    try {
      normalizeCliente(entity);
      ClienteDTO clienteActualizado = service.update(id, entity);
      // Crear/actualizar los contactos básicos si se proporcionaron
      crearContactosBasicos(clienteActualizado, telefono, tipoTelefono, email);
      addSuccessMessage(redirectAttributes, "Cliente actualizado exitosamente");
      return "redirect:/clientes";
    } catch (Exception e) {
      handleException(e, redirectAttributes, "actualizar");
      redirectAttributes.addFlashAttribute("cliente", entity);
      return "redirect:/clientes/" + id + "/editar";
    }
  }

  /**
   * Crea los contactos básicos (teléfono y email) para un cliente si se proporcionan.
   */
  private void crearContactosBasicos(ClienteDTO cliente, String telefono, String tipoTelefono, String email) {
    if (cliente == null || cliente.getId() == null) {
      return;
    }

    String personaId = "CLIENTE-" + cliente.getId();

    // Crear contacto telefónico si se proporcionó
    if (telefono != null && !telefono.trim().isEmpty()) {
      try {
        ContactoTelefonicoDTO contactoTelefonico = new ContactoTelefonicoDTO();
        contactoTelefonico.setTelefono(telefono.trim());
        // Usar el tipo de teléfono proporcionado, o CELULAR por defecto si no se especificó
        if (tipoTelefono != null && !tipoTelefono.trim().isEmpty()) {
          try {
            contactoTelefonico.setTipoTelefono(TipoTelefono.valueOf(tipoTelefono));
          } catch (IllegalArgumentException e) {
            logger.warn("Tipo de teléfono inválido '{}', usando CELULAR por defecto", tipoTelefono);
            contactoTelefonico.setTipoTelefono(TipoTelefono.CELULAR);
          }
        } else {
          contactoTelefonico.setTipoTelefono(TipoTelefono.CELULAR); // Por defecto CELULAR
        }
        contactoTelefonico.setTipoContacto(TipoContacto.PERSONAL);
        contactoTelefonico.setPersonaId(personaId);
        contactoTelefonicoService.create(contactoTelefonico);
        logger.info("Contacto telefónico creado para cliente ID: {}", cliente.getId());
      } catch (Exception e) {
        logger.warn("No se pudo crear el contacto telefónico para el cliente {}: {}", cliente.getId(), e.getMessage());
      }
    }

    // Crear contacto de correo electrónico si se proporcionó
    if (email != null && !email.trim().isEmpty()) {
      try {
        ContactoCorreoElectronicoDTO contactoCorreo = new ContactoCorreoElectronicoDTO();
        contactoCorreo.setEmail(email.trim());
        contactoCorreo.setTipoContacto(TipoContacto.PERSONAL);
        contactoCorreo.setPersonaId(personaId);
        contactoCorreoElectronicoService.create(contactoCorreo);
        logger.info("Contacto de correo electrónico creado para cliente ID: {}", cliente.getId());
      } catch (Exception e) {
        logger.warn("No se pudo crear el contacto de correo electrónico para el cliente {}: {}", cliente.getId(), e.getMessage());
      }
    }
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

