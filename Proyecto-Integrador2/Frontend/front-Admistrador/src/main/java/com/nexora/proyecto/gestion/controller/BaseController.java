package com.nexora.proyecto.gestion.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.nexora.proyecto.gestion.business.logic.service.BaseService;
import com.nexora.proyecto.gestion.dto.BaseDTO;

import jakarta.servlet.http.HttpSession;

/**
 * Controlador base genérico que proporciona funcionalidad común para todos los controladores.
 * Maneja redirecciones, mensajes de éxito/error y validación de sesión.
 * 
 * @param <T> Tipo del DTO
 * @param <ID> Tipo del identificador
 */
public abstract class BaseController<T extends BaseDTO, ID> {

  protected final Logger logger = LoggerFactory.getLogger(getClass());
  protected final BaseService<T, ID> service;
  protected final String entityName;
  protected final String entityPath;

  public BaseController(BaseService<T, ID> service, String entityName, String entityPath) {
    this.service = service;
    this.entityName = entityName;
    this.entityPath = entityPath;
  }

  /**
   * Verifica si hay una sesión activa. Si no hay sesión, redirige al login.
   */
  protected String checkSession(HttpSession session) {
    if (session.getAttribute("token") == null) {
      return "redirect:/auth/login";
    }
    return null;
  }

  /**
   * Agrega los atributos comunes de la sesión al modelo (nombreUsuario, rol).
   */
  protected void addSessionAttributesToModel(Model model, HttpSession session) {
    model.addAttribute("nombreUsuario", session.getAttribute("nombreUsuario"));
    model.addAttribute("rol", session.getAttribute("rol"));
  }

  /**
   * Agrega un mensaje de éxito a los atributos de redirección.
   */
  protected void addSuccessMessage(RedirectAttributes redirectAttributes, String message) {
    redirectAttributes.addFlashAttribute("success", message);
  }

  /**
   * Agrega un mensaje de error a los atributos de redirección.
   */
  protected void addErrorMessage(RedirectAttributes redirectAttributes, String message) {
    redirectAttributes.addFlashAttribute("error", message);
  }

  /**
   * Agrega un mensaje de error al modelo.
   */
  protected void addErrorToModel(Model model, String message) {
    model.addAttribute("error", message);
  }

  /**
   * Agrega un mensaje de éxito al modelo.
   */
  protected void addSuccessToModel(Model model, String message) {
    model.addAttribute("success", message);
  }

  /**
   * Maneja excepciones y agrega mensajes de error apropiados.
   */
  protected void handleException(Exception e, RedirectAttributes redirectAttributes, String operation) {
    logger.error("Error al {} {}: {}", operation, entityName, e.getMessage());
    addErrorMessage(redirectAttributes, "Error al " + operation + " " + entityName + ": " + e.getMessage());
  }

  /**
   * Maneja excepciones y agrega mensajes de error al modelo.
   */
  protected void handleExceptionToModel(Exception e, Model model, String operation) {
    logger.error("Error al {} {}: {}", operation, entityName, e.getMessage());
    addErrorToModel(model, "Error al " + operation + " " + entityName + ": " + e.getMessage());
  }

  /**
   * Lista todas las entidades activas.
   */
  @GetMapping
  public String listar(Model model, HttpSession session) {
    String redirect = checkSession(session);
    if (redirect != null) {
      return redirect;
    }
    try {
      addSessionAttributesToModel(model, session);
      // Convertir entityPath (puede tener guiones) a camelCase para el nombre del atributo
      String attributeName = toCamelCase(entityPath);
      model.addAttribute(attributeName, service.findAllActives());
      return entityPath + "/list";
    } catch (Exception e) {
      handleExceptionToModel(e, model, "listar");
      return entityPath + "/list";
    }
  }

  /**
   * Muestra el detalle de una entidad por ID.
   */
  @GetMapping("/{id}")
  public String detalle(@PathVariable ID id, Model model, HttpSession session) {
    String redirect = checkSession(session);
    if (redirect != null) {
      return redirect;
    }
    try {
      addSessionAttributesToModel(model, session);
      model.addAttribute(entityName, service.findById(id));
      return entityPath + "/detail";
    } catch (Exception e) {
      logger.error("Error al obtener {}: {}", entityName, e.getMessage());
      return "redirect:/" + entityPath + "?error=" + e.getMessage();
    }
  }

  /**
   * Muestra el formulario para crear una nueva entidad.
   */
  @GetMapping("/nuevo")
  public String mostrarFormularioNuevo(Model model, HttpSession session) {
    String redirect = checkSession(session);
    if (redirect != null) {
      return redirect;
    }
    try {
      addSessionAttributesToModel(model, session);
      // Asegurar que el objeto siempre esté en el modelo antes de llamar a prepareFormModel
      T entity = createNewEntity();
      model.addAttribute(entityName, entity);
      try {
        prepareFormModel(model);
      } catch (Exception e) {
        logger.error("Error al preparar modelo del formulario: {}", e.getMessage(), e);
        // Agregar el error al modelo pero continuar renderizando el formulario
        addErrorToModel(model, "Error al cargar datos del formulario: " + e.getMessage());
      }
      return entityPath + "/form";
    } catch (Exception e) {
      logger.error("Error al cargar formulario: {}", e.getMessage(), e);
      return "redirect:/" + entityPath + "?error=" + e.getMessage();
    }
  }

  /**
   * Muestra el formulario para editar una entidad existente.
   */
  @GetMapping("/{id}/editar")
  public String mostrarFormularioEditar(@PathVariable ID id, Model model, HttpSession session) {
    String redirect = checkSession(session);
    if (redirect != null) {
      return redirect;
    }
    try {
      addSessionAttributesToModel(model, session);
      // Asegurar que el objeto siempre esté en el modelo antes de llamar a prepareFormModel
      T entity = service.findById(id);
      model.addAttribute(entityName, entity);
      try {
        prepareFormModel(model);
      } catch (Exception e) {
        logger.error("Error al preparar modelo del formulario: {}", e.getMessage(), e);
        // Agregar el error al modelo pero continuar renderizando el formulario
        addErrorToModel(model, "Error al cargar datos del formulario: " + e.getMessage());
      }
      return entityPath + "/form";
    } catch (Exception e) {
      logger.error("Error al cargar {} para editar: {}", entityName, e.getMessage(), e);
      return "redirect:/" + entityPath + "?error=" + e.getMessage();
    }
  }

  /**
   * Crea una nueva entidad.
   */
  @PostMapping
  public String crear(T entity, RedirectAttributes redirectAttributes, HttpSession session) {
    String redirect = checkSession(session);
    if (redirect != null) {
      return redirect;
    }
    try {
      service.create(entity);
      addSuccessMessage(redirectAttributes, capitalize(entityName) + " creado exitosamente");
      return "redirect:/" + entityPath;
    } catch (Exception e) {
      handleException(e, redirectAttributes, "crear");
      redirectAttributes.addFlashAttribute(entityPath.substring(0, entityPath.length() - 1), entity);
      return "redirect:/" + entityPath + "/nuevo";
    }
  }

  /**
   * Actualiza una entidad existente.
   */
  @PostMapping("/{id}")
  public String actualizar(@PathVariable ID id, T entity, RedirectAttributes redirectAttributes, HttpSession session) {
    String redirect = checkSession(session);
    if (redirect != null) {
      return redirect;
    }
    try {
      // El ID ya viene en el path, no es necesario setearlo en la entidad
      service.update(id, entity);
      addSuccessMessage(redirectAttributes, capitalize(entityName) + " actualizado exitosamente");
      return "redirect:/" + entityPath;
    } catch (Exception e) {
      handleException(e, redirectAttributes, "actualizar");
      return "redirect:/" + entityPath + "/" + id + "/editar";
    }
  }

  /**
   * Elimina una entidad (eliminación lógica).
   */
  @PostMapping("/{id}/eliminar")
  public String eliminar(@PathVariable ID id, RedirectAttributes redirectAttributes, HttpSession session) {
    String redirect = checkSession(session);
    if (redirect != null) {
      return redirect;
    }
    try {
      service.delete(id);
      addSuccessMessage(redirectAttributes, capitalize(entityName) + " eliminado exitosamente");
      return "redirect:/" + entityPath;
    } catch (Exception e) {
      handleException(e, redirectAttributes, "eliminar");
      return "redirect:/" + entityPath;
    }
  }

  /**
   * Crea una nueva instancia de la entidad. Debe ser sobrescrito por subclases.
   */
  protected abstract T createNewEntity();

  /**
   * Prepara el modelo para el formulario. Puede ser sobrescrito por subclases
   * para agregar datos adicionales necesarios para el formulario.
   */
  protected void prepareFormModel(Model model) {
    // Implementación por defecto: no hace nada
    // Las subclases pueden sobrescribir este método para agregar datos adicionales
  }

  /**
   * Capitaliza la primera letra de una cadena.
   */
  private String capitalize(String str) {
    if (str == null || str.isEmpty()) {
      return str;
    }
    return str.substring(0, 1).toUpperCase() + str.substring(1);
  }

  /**
   * Convierte una cadena en kebab-case a camelCase.
   * Ejemplo: "costos-vehiculo" -> "costosVehiculo"
   */
  private String toCamelCase(String str) {
    if (str == null || str.isEmpty()) {
      return str;
    }
    if (!str.contains("-")) {
      return str;
    }
    String[] parts = str.split("-");
    StringBuilder result = new StringBuilder(parts[0]);
    for (int i = 1; i < parts.length; i++) {
      if (!parts[i].isEmpty()) {
        result.append(parts[i].substring(0, 1).toUpperCase());
        if (parts[i].length() > 1) {
          result.append(parts[i].substring(1));
        }
      }
    }
    return result.toString();
  }

}

