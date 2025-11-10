package com.nexora.proyecto.gestion.controller;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.nexora.proyecto.gestion.business.logic.service.EmpleadoService;
import com.nexora.proyecto.gestion.business.logic.service.LocalidadService;
import com.nexora.proyecto.gestion.business.logic.service.UsuarioService;
import com.nexora.proyecto.gestion.dto.DireccionDTO;
import com.nexora.proyecto.gestion.dto.EmpleadoDTO;
import com.nexora.proyecto.gestion.dto.UsuarioDTO;
import com.nexora.proyecto.gestion.dto.enums.RolUsuario;
import com.nexora.proyecto.gestion.dto.enums.TipoEmpleado;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/empleados")
public class EmpleadoController extends BaseController<EmpleadoDTO, String> {

  private final LocalidadService localidadService;
  private final EmpleadoService empleadoService;
  private final UsuarioService usuarioService;

  public EmpleadoController(EmpleadoService empleadoService, LocalidadService localidadService, UsuarioService usuarioService) {
    super(empleadoService, "empleado", "empleados");
    this.empleadoService = empleadoService;
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
  protected EmpleadoDTO createNewEntity() {
    EmpleadoDTO empleado = new EmpleadoDTO();
    // Inicializar dirección y contacto para evitar NullPointerException
    if (empleado.getDireccion() == null) {
      empleado.setDireccion(new DireccionDTO());
    }
    // Los contactos se manejarán como lista más adelante
    return empleado;
  }

  @Override
  protected void prepareFormModel(Model model) {
    try {
      model.addAttribute("localidades", localidadService.findAllActives());
      model.addAttribute("tiposEmpleado", TipoEmpleado.values());
      model.addAttribute("rolesUsuario", RolUsuario.values());
    } catch (Exception e) {
      logger.error("Error al preparar modelo del formulario: {}", e.getMessage());
    }
  }

  /**
   * Sobrescribe el método crear para agregar validación de rol JEFE y creación de usuario
   */
  @Override
  @PostMapping
  public String crear(EmpleadoDTO entity, RedirectAttributes redirectAttributes, HttpSession session) {
    String redirect = checkSession(session);
    if (redirect != null) {
      return redirect;
    }
    try {
      // Validar que solo JEFE puede crear empleados ADMINISTRATIVO
      String rolUsuario = (String) session.getAttribute("rol");
      empleadoService.validateTipoEmpleado(entity, rolUsuario);
      
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
        nuevoUsuario.setRol(RolUsuario.valueOf(nuevoRol));
        
        UsuarioDTO usuarioCreado = usuarioService.create(nuevoUsuario);
        entity.setUsuarioId(usuarioCreado.getId());
      }
      
      service.create(entity);
      addSuccessMessage(redirectAttributes, "Empleado creado exitosamente");
      return "redirect:/" + entityPath;
    } catch (Exception e) {
      handleException(e, redirectAttributes, "crear");
      redirectAttributes.addFlashAttribute(entityPath.substring(0, entityPath.length() - 1), entity);
      return "redirect:/" + entityPath + "/nuevo";
    }
  }

  /**
   * Sobrescribe el método actualizar para agregar validación de rol JEFE
   */
  @Override
  @PostMapping("/{id}")
  public String actualizar(String id, EmpleadoDTO entity, RedirectAttributes redirectAttributes, HttpSession session) {
    String redirect = checkSession(session);
    if (redirect != null) {
      return redirect;
    }
    try {
      // Validar que solo JEFE puede cambiar a ADMINISTRATIVO
      String rolUsuario = (String) session.getAttribute("rol");
      
      // Obtener el empleado actual para comparar
      EmpleadoDTO empleadoActual = service.findById(id);
      // Si el tipo cambió a ADMINISTRATIVO, validar que el usuario sea JEFE
      if (empleadoActual.getTipoEmpleado() != entity.getTipoEmpleado() && 
          entity.getTipoEmpleado() == TipoEmpleado.ADMINISTRATIVO) {
        empleadoService.validateTipoEmpleado(entity, rolUsuario);
      }
      
      service.update(id, entity);
      addSuccessMessage(redirectAttributes, "Empleado actualizado exitosamente");
      return "redirect:/" + entityPath;
    } catch (Exception e) {
      handleException(e, redirectAttributes, "actualizar");
      return "redirect:/" + entityPath + "/" + id + "/editar";
    }
  }
}

