package com.nexora.proyecto.gestion.controller;

import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.nexora.proyecto.gestion.business.logic.service.EmpleadoService;
import com.nexora.proyecto.gestion.dto.EmpleadoDTO;
import com.nexora.proyecto.gestion.dto.enums.RolUsuario;

import jakarta.servlet.http.HttpSession;


@Controller
@RequestMapping("/perfil")
public class PerfilController {

  private static final Logger logger = LoggerFactory.getLogger(PerfilController.class);

  private final EmpleadoService empleadoService;

  public PerfilController(EmpleadoService empleadoService) {
    this.empleadoService = empleadoService;
  }

  @GetMapping
  public String mostrarPerfil(HttpSession session, Model model) {
    // Verificar sesión
    if (session.getAttribute("token") == null) {
      return "redirect:/auth/login";
    }

    String rol = (String) session.getAttribute("rol");
    
    // Validar que el usuario no sea CLIENTE (solo empleados pueden acceder al sitio administrativo)
    if (rol != null && rol.equals(RolUsuario.CLIENTE.toString())) {
      logger.warn("Intento de acceso a perfil por usuario CLIENTE en sitio administrativo");
      model.addAttribute("error", "Los usuarios de tipo CLIENTE no pueden acceder al sitio administrativo");
      return "redirect:/auth/logout";
    }

    String usuarioId = (String) session.getAttribute("usuarioId");
    if (usuarioId == null) {
      logger.warn("No se encontró usuarioId en la sesión");
      model.addAttribute("error", "No se pudo identificar al usuario");
      return "perfil";
    }

    logger.info("🔍 Buscando perfil para usuarioId: {}, rol: {}, nombreUsuario: {}", 
        usuarioId, rol, session.getAttribute("nombreUsuario"));

    try {
      // Agregar atributos de sesión al modelo
      model.addAttribute("nombreUsuario", session.getAttribute("nombreUsuario"));
      model.addAttribute("rol", rol);

      // Buscar empleado asociado al usuario (solo empleados pueden estar en el sitio administrativo)
      EmpleadoDTO empleado = buscarEmpleadoPorUsuarioId(usuarioId);
      if (empleado != null) {
        // Limpiar datos sensibles antes de mostrar
        limpiarDatosSensibles(empleado);
        model.addAttribute("persona", empleado);
        model.addAttribute("tipoPersona", "empleado");
        logger.info("Perfil de empleado encontrado para usuarioId: {}", usuarioId);
        return "perfil";
      }

      // Si no se encontró empleado
      logger.warn("No se encontró empleado asociado al usuarioId: {}", usuarioId);
      model.addAttribute("error", "No se encontraron datos de perfil asociados a tu usuario. Solo los empleados pueden acceder al sitio administrativo.");
      return "perfil";

    } catch (Exception e) {
      logger.error("Error al obtener perfil para usuarioId {}: {}", usuarioId, e.getMessage(), e);
      model.addAttribute("error", "Error al cargar el perfil: " + e.getMessage());
      return "perfil";
    }
  }

  /**
   * Busca un empleado por el ID de su usuario asociado.
   */
  private EmpleadoDTO buscarEmpleadoPorUsuarioId(String usuarioId) {
    try {
      logger.debug("Buscando empleado para usuarioId: {}", usuarioId);
      Collection<EmpleadoDTO> empleados = empleadoService.findAllActives();
      logger.debug("Total de empleados encontrados: {}", empleados != null ? empleados.size() : 0);
      
      if (empleados != null) {
        // Log de todos los empleados y sus usuarios para debugging
        empleados.forEach(e -> {
          if (e.getUsuario() != null) {
            logger.debug("Empleado: {} {} - UsuarioId: {}", 
                e.getNombre(), e.getApellido(), e.getUsuario().getId());
          } else {
            logger.debug("Empleado: {} {} - Sin usuario asociado", e.getNombre(), e.getApellido());
          }
        });
        
        EmpleadoDTO encontrado = empleados.stream()
            .filter(e -> {
              if (e.getUsuario() == null) {
                logger.debug("Empleado {} {} no tiene usuario asociado", e.getNombre(), e.getApellido());
                return false;
              }
              String empleadoUsuarioId = e.getUsuario().getId();
              boolean coincide = usuarioId.equals(empleadoUsuarioId);
              logger.debug("Comparando usuarioId sesión: '{}' con empleado usuarioId: '{}' -> coincide: {}", 
                  usuarioId, empleadoUsuarioId, coincide);
              return coincide;
            })
            .findFirst()
            .orElse(null);
        
        if (encontrado != null) {
          logger.info("✅ Empleado encontrado: {} {} para usuarioId: {}", 
              encontrado.getNombre(), encontrado.getApellido(), usuarioId);
        } else {
          logger.warn("❌ No se encontró empleado para usuarioId: {}", usuarioId);
        }
        
        return encontrado;
      }
    } catch (Exception e) {
      logger.error("Error al buscar empleado por usuarioId {}: {}", usuarioId, e.getMessage(), e);
    }
    return null;
  }

  /**
   * Limpia datos sensibles del empleado antes de mostrarlos en la vista.
   */
  private void limpiarDatosSensibles(EmpleadoDTO empleado) {
    if (empleado != null && empleado.getUsuario() != null) {
      // No exponer la contraseña
      empleado.getUsuario().setClave(null);
    }
  }

}

