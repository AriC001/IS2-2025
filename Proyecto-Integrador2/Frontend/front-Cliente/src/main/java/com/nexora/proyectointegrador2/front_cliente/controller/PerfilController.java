package com.nexora.proyectointegrador2.front_cliente.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.nexora.proyectointegrador2.front_cliente.business.logic.service.ClienteService;
import com.nexora.proyectointegrador2.front_cliente.business.logic.service.DireccionService;
import com.nexora.proyectointegrador2.front_cliente.business.logic.service.LocalidadService;
import com.nexora.proyectointegrador2.front_cliente.business.logic.service.NacionalidadService;
import com.nexora.proyectointegrador2.front_cliente.dto.ClienteDTO;
import com.nexora.proyectointegrador2.front_cliente.dto.DireccionDTO;
import com.nexora.proyectointegrador2.front_cliente.dto.LocalidadDTO;
import com.nexora.proyectointegrador2.front_cliente.dto.NacionalidadDTO;

import jakarta.servlet.http.HttpSession;

/**
 * Controlador para manejar las rutas /mi-perfil y /mi-perfil/guardar
 * que son usadas por las vistas del frontend cliente
 */
@Controller
@RequestMapping("/mi-perfil")  // Cambiado a /mi-perfil para evitar conflicto con BaseController
public class PerfilController extends BaseController<ClienteDTO, String> {

  private final ClienteService clienteService;
  private final NacionalidadService nacionalidadService;
  private final LocalidadService localidadService;
  private final DireccionService direccionService;

  public PerfilController(ClienteService clienteService, 
                          NacionalidadService nacionalidadService,
                          LocalidadService localidadService,
                          DireccionService direccionService) {
    super(clienteService, "cliente", "clientes");
    this.clienteService = clienteService;
    this.nacionalidadService = nacionalidadService;
    this.localidadService = localidadService;
    this.direccionService = direccionService;
  }

  /**
   * Muestra el perfil del cliente logueado
   * Ruta: /mi-perfil (usada por las vistas)
   */
  @GetMapping
  public String verPerfil(Model model, HttpSession session) {
    String redirect = checkSession(session);
    if (redirect != null) {
      return redirect;
    }

    try {
      addSessionAttributesToModel(model, session);

      // Obtener nombreUsuario desde la sesión
      Object nombreUsuarioAttr = session.getAttribute("nombreUsuario");
      if (nombreUsuarioAttr == null) {
        logger.warn("No se encontró nombreUsuario en la sesión");
        addErrorToModel(model, "No se pudo identificar al usuario");
        return "redirect:/home";
      }

      String nombreUsuario = nombreUsuarioAttr.toString();
      logger.debug("Buscando cliente para nombreUsuario: {}", nombreUsuario);
      
      // Buscar cliente por nombreUsuario
      ClienteDTO cliente;
      try {
        cliente = clienteService.findByNombreUsuario(nombreUsuario);
        logger.debug("Cliente encontrado: {}", cliente != null ? cliente.getId() : "null");
      } catch (Exception e) {
        logger.error("Error al buscar cliente por nombreUsuario {}: {}", nombreUsuario, e.getMessage(), e);
        throw e; // Re-lanzar para que sea capturado por el catch general
      }
      
      if (cliente == null) {
        logger.warn("No se encontró cliente con nombreUsuario: {}", nombreUsuario);
        addErrorToModel(model, "No se encontró información del cliente. Por favor, contacte al administrador.");
        // Crear un cliente vacío para que el formulario se pueda mostrar
        cliente = createNewEntity();
        cliente.setNombre("");
        cliente.setApellido("");
        cliente.setDireccion(new DireccionDTO());
        cliente.getDireccion().setLocalidad(new LocalidadDTO());
        cliente.setNacionalidad(new NacionalidadDTO());
      } else {
        // Inicializar objetos anidados si no existen
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
      }

      model.addAttribute("cliente", cliente);
      try {
        model.addAttribute("localidades", localidadService.findAllActives());
        model.addAttribute("nacionalidades", nacionalidadService.findAllActives());
      } catch (Exception e) {
        logger.warn("Error al cargar localidades o nacionalidades: {}", e.getMessage());
        model.addAttribute("localidades", java.util.Collections.emptyList());
        model.addAttribute("nacionalidades", java.util.Collections.emptyList());
      }

      return "perfil";
    } catch (Exception e) {
      logger.error("Error al obtener perfil: {}", e.getMessage(), e);
      addErrorToModel(model, "Error al cargar el perfil: " + e.getMessage() + ". Por favor, intente nuevamente.");
      // Crear un cliente vacío para que el formulario se pueda mostrar
      ClienteDTO clienteVacio = createNewEntity();
      clienteVacio.setNombre("");
      clienteVacio.setApellido("");
      clienteVacio.setDireccion(new DireccionDTO());
      clienteVacio.getDireccion().setLocalidad(new LocalidadDTO());
      clienteVacio.setNacionalidad(new NacionalidadDTO());
      model.addAttribute("cliente", clienteVacio);
      try {
        model.addAttribute("localidades", localidadService.findAllActives());
        model.addAttribute("nacionalidades", nacionalidadService.findAllActives());
      } catch (Exception ex) {
        logger.warn("Error al cargar localidades o nacionalidades: {}", ex.getMessage());
        model.addAttribute("localidades", java.util.Collections.emptyList());
        model.addAttribute("nacionalidades", java.util.Collections.emptyList());
      }
      return "perfil";
    }
  }

  /**
   * Guarda los cambios del perfil del cliente
   * Ruta: /perfil/guardar (usada por el formulario de la vista)
   * Nota: Cambiado de /cliente/perfil/guardar a /perfil/guardar para mantener consistencia
   */
  @PostMapping("/guardar")
  public String guardarPerfil(@ModelAttribute ClienteDTO cliente, 
                              RedirectAttributes redirectAttributes,
                              HttpSession session) {
    String redirect = checkSession(session);
    if (redirect != null) {
      return redirect;
    }

    try {
      // Obtener nombreUsuario desde la sesión
      Object nombreUsuarioAttr = session.getAttribute("nombreUsuario");
      if (nombreUsuarioAttr == null) {
        logger.warn("No se encontró nombreUsuario en la sesión");
        addErrorMessage(redirectAttributes, "No se pudo identificar al usuario");
        return "redirect:/home";
      }

      String nombreUsuario = nombreUsuarioAttr.toString();
      
      // Buscar cliente actual por nombreUsuario para obtener su ID
      ClienteDTO clienteActual = clienteService.findByNombreUsuario(nombreUsuario);
      
      if (clienteActual == null) {
        logger.warn("No se encontró cliente con nombreUsuario: {}", nombreUsuario);
        addErrorMessage(redirectAttributes, "No se encontró información del cliente");
        return "redirect:/home";
      }

      // Asignar el ID del cliente actual al cliente recibido del formulario
      cliente.setId(clienteActual.getId());
      
      // Mantener el usuario asociado (no se debe cambiar)
      if (clienteActual.getUsuario() != null) {
        cliente.setUsuario(clienteActual.getUsuario());
      }

      // Normalizar datos antes de actualizar (sanitizar direcciones y relaciones vacías)
      if (cliente.getDireccion() != null && 
          (cliente.getDireccion().getId() == null || cliente.getDireccion().getId().trim().isEmpty())) {
        cliente.setDireccion(null);
      }
      if (cliente.getUsuario() != null && 
          (cliente.getUsuario().getId() == null || cliente.getUsuario().getId().trim().isEmpty())) {
        cliente.setUsuario(null);
      }
      if (cliente.getNacionalidad() != null && 
          (cliente.getNacionalidad().getId() == null || cliente.getNacionalidad().getId().trim().isEmpty())) {
        cliente.setNacionalidad(null);
      }

      // Actualizar el cliente
      clienteService.update(cliente.getId(), cliente);
      
      addSuccessMessage(redirectAttributes, "Perfil actualizado exitosamente");
      return "redirect:/mi-perfil";
    } catch (Exception e) {
      logger.error("Error al actualizar perfil: {}", e.getMessage(), e);
      handleException(e, redirectAttributes, "actualizar");
      return "redirect:/mi-perfil";
    }
  }

  @Override
  protected ClienteDTO createNewEntity() {
    return new ClienteDTO();
  }

  /**
   * Sobrescribe el método listar() de BaseController para evitar conflicto.
   * Mapea a /mi-perfil/list para que no entre en conflicto con verPerfil().
   */
  @Override
  @GetMapping("/list")
  public String listar(Model model, HttpSession session) {
    // Redirigir a verPerfil() ya que este controlador maneja el perfil del usuario
    return verPerfil(model, session);
  }
}

