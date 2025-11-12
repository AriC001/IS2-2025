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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.nexora.proyecto.gestion.business.logic.service.DireccionService;
import com.nexora.proyecto.gestion.business.logic.service.EmpleadoService;
import com.nexora.proyecto.gestion.business.logic.service.LocalidadService;
import com.nexora.proyecto.gestion.business.logic.service.UsuarioService;
import com.nexora.proyecto.gestion.dto.DireccionDTO;
import com.nexora.proyecto.gestion.dto.EmpleadoDTO;
import com.nexora.proyecto.gestion.dto.ImagenDTO;
import com.nexora.proyecto.gestion.dto.LocalidadDTO;
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
  private final DireccionService direccionService;

  public EmpleadoController(EmpleadoService empleadoService, LocalidadService localidadService,
      UsuarioService usuarioService, DireccionService direccionService) {
    super(empleadoService, "empleado", "empleados");
    this.empleadoService = empleadoService;
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
  protected EmpleadoDTO createNewEntity() {
    EmpleadoDTO empleado = new EmpleadoDTO();
    if (empleado.getDireccion() == null) {
      DireccionDTO direccion = new DireccionDTO();
      direccion.setLocalidad(new LocalidadDTO());
      empleado.setDireccion(direccion);
    }
    if (empleado.getUsuario() == null) {
      empleado.setUsuario(new UsuarioDTO());
    }
    if (empleado.getImagenPerfil() == null) {
      empleado.setImagenPerfil(new ImagenDTO());
    }
    // Los contactos se manejarán como lista más adelante
    return empleado;
  }

  @Override
  protected void prepareFormModel(Model model) {
    try {
      EmpleadoDTO empleado = (EmpleadoDTO) model.getAttribute("empleado");
      if (empleado != null) {
        if (empleado.getDireccion() == null) {
          DireccionDTO direccion = new DireccionDTO();
          direccion.setLocalidad(new LocalidadDTO());
          empleado.setDireccion(direccion);
        } else if (empleado.getDireccion().getLocalidad() == null) {
          empleado.getDireccion().setLocalidad(new LocalidadDTO());
        }
        if (empleado.getUsuario() == null) {
          empleado.setUsuario(new UsuarioDTO());
        }
        if (empleado.getImagenPerfil() == null) {
          empleado.setImagenPerfil(new ImagenDTO());
        }
      }
    } catch (Exception e) {
      logger.error("Error al inicializar empleado en el modelo: {}", e.getMessage(), e);
    }

    // Cargar direcciones con manejo de errores específico
    try {
      List<DireccionDTO> direcciones = direccionService.findAllActives();
      logger.debug("Cargadas {} direcciones para el formulario de empleado", direcciones != null ? direcciones.size() : 0);
      model.addAttribute("direcciones", direcciones != null ? direcciones : Collections.emptyList());
    } catch (Exception e) {
      logger.error("Error al cargar direcciones para el formulario de empleado: {}", e.getMessage(), e);
      logger.error("Stack trace completo:", e);
      // Asegurar que siempre haya un atributo direcciones en el modelo, incluso si está vacío
      model.addAttribute("direcciones", Collections.emptyList());
    }

    // Cargar localidades con manejo de errores específico
    try {
      List<LocalidadDTO> localidades = localidadService.findAllActives();
      logger.debug("Cargadas {} localidades para el formulario de empleado", localidades != null ? localidades.size() : 0);
      model.addAttribute("localidades", localidades != null ? localidades : Collections.emptyList());
    } catch (Exception e) {
      logger.error("Error al cargar localidades para el formulario de empleado: {}", e.getMessage(), e);
      model.addAttribute("localidades", Collections.emptyList());
    }

    // Agregar enums (estos nunca fallan)
    model.addAttribute("tiposEmpleado", TipoEmpleado.values());
    model.addAttribute("rolesUsuario", RolUsuario.values());
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
      sanitizeDireccion(entity);
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
          nuevoRol != null && !nuevoRol.trim().isEmpty()) {
        UsuarioDTO nuevoUsuario = new UsuarioDTO();
        nuevoUsuario.setNombreUsuario(nuevoNombreUsuario.trim());
        // Siempre usar "mycar" como contraseña por defecto para nuevos empleados
        nuevoUsuario.setClave("mycar");
        nuevoUsuario.setRol(RolUsuario.valueOf(nuevoRol));
        
        logger.info("Creando nuevo usuario '{}' con contraseña por defecto 'mycar'", nuevoNombreUsuario);
        
        UsuarioDTO usuarioCreado = usuarioService.create(nuevoUsuario);
        entity.setUsuario(usuarioCreado);
      }

      sanitizeUsuario(entity);
      
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
      sanitizeDireccion(entity);
      sanitizeUsuario(entity);
      
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

  private void sanitizeDireccion(EmpleadoDTO entity) {
    if (entity.getDireccion() != null && !StringUtils.hasText(entity.getDireccion().getId())) {
      entity.setDireccion(null);
    }
  }

  private void sanitizeUsuario(EmpleadoDTO entity) {
    if (entity.getUsuario() != null && !StringUtils.hasText(entity.getUsuario().getId())) {
      entity.setUsuario(null);
    }
  }
}

