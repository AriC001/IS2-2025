package com.nexora.proyectointegrador2.front_cliente.controller;

import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.nexora.proyectointegrador2.front_cliente.business.logic.service.ClienteService;
import com.nexora.proyectointegrador2.front_cliente.business.logic.service.DireccionService;
import com.nexora.proyectointegrador2.front_cliente.business.logic.service.LocalidadService;
import com.nexora.proyectointegrador2.front_cliente.business.logic.service.NacionalidadService;
import com.nexora.proyectointegrador2.front_cliente.business.logic.service.UsuarioService;
import com.nexora.proyectointegrador2.front_cliente.dto.ClienteDTO;
import com.nexora.proyectointegrador2.front_cliente.dto.DireccionDTO;
import com.nexora.proyectointegrador2.front_cliente.dto.EmpleadoDTO;
import com.nexora.proyectointegrador2.front_cliente.dto.LocalidadDTO;
import com.nexora.proyectointegrador2.front_cliente.dto.NacionalidadDTO;
import com.nexora.proyectointegrador2.front_cliente.dto.UsuarioDTO;
import com.nexora.proyectointegrador2.front_cliente.dto.enums.RolUsuario;
import com.nexora.proyectointegrador2.front_cliente.dto.enums.TipoEmpleado;

import jakarta.servlet.http.HttpServletRequest;
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
  
   /**
   * Sobrescribe el método crear para agregar validación de rol JEFE y creación de usuario
   */


  @PostMapping("/registro")
  public String crear(ClienteDTO entity, RedirectAttributes redirectAttributes) {
 
    try {
      sanitizeDireccion(entity);
      
      // Obtener parámetros del request para crear usuario
      ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
      String nuevoNombreUsuario = null;
      String nuevaClave = null;
      String nuevoRol = null;
      
      if (attributes != null) {
        HttpServletRequest request = attributes.getRequest();
        nuevoNombreUsuario = request.getParameter("nuevoNombreUsuario");
        nuevaClave = request.getParameter("nuevaClave");
        nuevoRol = request.getParameter("nuevoRol");
      }
      
      // Si se proporcionaron datos para crear un nuevo usuario, crearlo primero
      if (nuevoNombreUsuario != null && !nuevoNombreUsuario.trim().isEmpty() &&
          nuevaClave != null && !nuevaClave.trim().isEmpty() &&
          nuevoRol != null && !nuevoRol.trim().isEmpty()) {
        UsuarioDTO nuevoUsuario = new UsuarioDTO();
        nuevoUsuario.setNombreUsuario(nuevoNombreUsuario.trim());
        nuevoUsuario.setClave(nuevaClave);
        nuevoUsuario.setRol(RolUsuario.CLIENTE);
        
        UsuarioDTO usuarioCreado = usuarioService.create(nuevoUsuario);
        entity.setUsuario(usuarioCreado);
      }

      sanitizeUsuario(entity);
      
      service.create(entity);
      addSuccessMessage(redirectAttributes, "Usuario creado exitosamente");
      return "redirect:/" + entityPath;
    } catch (Exception e) {
      handleException(e, redirectAttributes, "crear");
      redirectAttributes.addFlashAttribute(entityPath.substring(0, entityPath.length() - 1), entity);
      return "redirect:/" + entityPath + "/nuevo";
    }

  }
  private void sanitizeDireccion(ClienteDTO entity) {
    if (entity.getDireccion() != null && !StringUtils.hasText(entity.getDireccion().getId())) {
      entity.setDireccion(null);
    }
  }

  private void sanitizeUsuario(ClienteDTO entity) {
    if (entity.getUsuario() != null && !StringUtils.hasText(entity.getUsuario().getId())) {
      entity.setUsuario(null);
    }
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
  @GetMapping("/registrar")
  public String formRegistro(Model model){
    try{
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
        return "registro";
      } catch (Exception e) {
          logger.error("Error al preparar modelo del formulario: {}", e.getMessage());
          return "landing";
    }
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
    } catch (Exception e) {
      logger.error("Error al preparar modelo del formulario: {}", e.getMessage());
    }
  }
  /** *
  @GetMapping("/perfil")
  public String verPerfil(Model model, Principal principal) {

      ClienteDTO cliente = clienteService.findByNombreUsuario(principal.getName());

      model.addAttribute("cliente", cliente);
      model.addAttribute("localidades", localidadService.findAllActives());
      model.addAttribute("nacionalidades", nacionalidadService.findAllActives());

      return "perfil";
  }
  **/


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

