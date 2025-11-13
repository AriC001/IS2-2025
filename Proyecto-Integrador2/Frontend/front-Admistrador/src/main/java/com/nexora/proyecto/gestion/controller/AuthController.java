package com.nexora.proyecto.gestion.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.nexora.proyecto.gestion.business.logic.service.AuthService;
import com.nexora.proyecto.gestion.dto.AuthResponseDTO;
import com.nexora.proyecto.gestion.dto.LoginRequestDTO;
import com.nexora.proyecto.gestion.dto.enums.RolUsuario;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/auth")
public class AuthController {

  private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
  
  private final AuthService authService;

  public AuthController(AuthService authService) {
    this.authService = authService;
  }

  @GetMapping("/login")
  public String mostrarLogin(
      @RequestParam(required = false) String error,
      @RequestParam(required = false) String logout,
      Model model) {
    
    if (error != null) {
      model.addAttribute("error", "Usuario o contraseña incorrectos");
    }
    
    if (logout != null) {
      model.addAttribute("message", "Has cerrado sesión correctamente");
    }
    
    return "login";
  }

  @PostMapping("/login")
  public String procesarLogin(
      @RequestParam String nombreUsuario,
      @RequestParam String clave,
      @RequestParam(required = false) boolean recordarme,
      HttpSession session,
      Model model) {
    
    try {
      logger.info("Intento de login para usuario: {}", nombreUsuario);
      
      LoginRequestDTO loginRequest = LoginRequestDTO.builder()
          .nombreUsuario(nombreUsuario)
          .clave(clave)
          .build();
      
      AuthResponseDTO authResponse = authService.login(loginRequest);
      
      // Validar que el usuario no sea CLIENTE
      if (authResponse.getRol() == RolUsuario.CLIENTE) {
        logger.warn("Intento de login rechazado: usuario {} con rol CLIENTE intentó acceder al sitio administrativo", nombreUsuario);
        model.addAttribute("error", "Los usuarios de tipo CLIENTE no pueden acceder al sitio administrativo");
        return "login";
      }
      
      // Verificar si requiere cambio de contraseña
      if (authResponse.getRequiereCambioClave() != null && authResponse.getRequiereCambioClave()) {
        logger.warn("⚠ Usuario {} tiene contraseña por defecto, redirigiendo a cambio de contraseña", nombreUsuario);
        // Guardar información en la sesión (necesaria para el cambio de contraseña)
        session.setAttribute("token", authResponse.getToken());
        session.setAttribute("usuarioId", authResponse.getId());
        session.setAttribute("nombreUsuario", authResponse.getNombreUsuario());
        session.setAttribute("rol", authResponse.getRol().toString());
        session.setAttribute("requiereCambioClave", true);
        return "redirect:/auth/cambiar-clave";
      }
      
      // Guardar información en la sesión
      session.setAttribute("token", authResponse.getToken());
      session.setAttribute("usuarioId", authResponse.getId());
      session.setAttribute("nombreUsuario", authResponse.getNombreUsuario());
      session.setAttribute("rol", authResponse.getRol().toString());
      session.setAttribute("requiereCambioClave", false);
      
      logger.info("✅ Login exitoso para usuario: {} con rol: {}", nombreUsuario, authResponse.getRol());
      
      return "redirect:/dashboard";
      
    } catch (Exception e) {
      logger.error("Error en el login: {}", e.getMessage());
      model.addAttribute("error", "Usuario o contraseña incorrectos");
      return "login";
    }
  }

  @GetMapping("/logout")
  public String logout(HttpSession session) {
    logger.info("Usuario {} cerrando sesión", session.getAttribute("nombreUsuario"));
    session.invalidate();
    return "redirect:/auth/login?logout";
  }

  @GetMapping("/forgot-password")
  public String mostrarRecuperarPassword() {
    return "forgot-password";
  }

  @GetMapping("/cambiar-clave")
  public String mostrarCambiarClave(HttpSession session, Model model) {
    // Verificar sesión
    if (session.getAttribute("token") == null) {
      return "redirect:/auth/login";
    }
    
    // Agregar información del usuario al modelo para mostrar en la vista
    String nombreUsuario = (String) session.getAttribute("nombreUsuario");
    if (nombreUsuario != null) {
      model.addAttribute("nombreUsuario", nombreUsuario);
    }
    
    // Permitir acceso a la vista de cambiar clave si tiene sesión válida
    // El flag requiereCambioClave solo se usa para forzar el cambio después del login
    return "cambiar-clave";
  }

  @PostMapping("/cambiar-clave")
  public String procesarCambiarClave(
      @RequestParam String nuevaClave,
      @RequestParam String confirmarClave,
      HttpSession session,
      Model model) {
    
    // Verificar sesión
    if (session.getAttribute("token") == null) {
      return "redirect:/auth/login";
    }
    
    try {
      // Validaciones
      if (nuevaClave == null || nuevaClave.trim().isEmpty()) {
        model.addAttribute("error", "La nueva contraseña es obligatoria");
        return "cambiar-clave";
      }
      
      if (nuevaClave.length() < 6) {
        model.addAttribute("error", "La contraseña debe tener al menos 6 caracteres");
        return "cambiar-clave";
      }
      
      if (!nuevaClave.equals(confirmarClave)) {
        model.addAttribute("error", "Las contraseñas no coinciden");
        return "cambiar-clave";
      }
      
      // Validar que no sea la contraseña por defecto
      if (nuevaClave.equals("mycar")) {
        model.addAttribute("error", "No se puede usar la contraseña por defecto");
        return "cambiar-clave";
      }
      
      String usuarioId = (String) session.getAttribute("usuarioId");
      if (usuarioId == null) {
        model.addAttribute("error", "No se pudo identificar al usuario");
        return "cambiar-clave";
      }
      
      // Llamar al servicio para cambiar la contraseña
      String token = (String) session.getAttribute("token");
      authService.cambiarClave(usuarioId, nuevaClave, token);
      
      // Limpiar el flag de cambio de contraseña requerido
      session.setAttribute("requiereCambioClave", false);
      
      logger.info("✅ Contraseña cambiada exitosamente para usuario ID: {}", usuarioId);
      model.addAttribute("success", "Contraseña cambiada exitosamente. Serás redirigido al dashboard.");
      
      // Redirigir al dashboard después de 2 segundos
      return "redirect:/dashboard";
      
    } catch (Exception e) {
      logger.error("Error al cambiar contraseña: {}", e.getMessage());
      model.addAttribute("error", "Error al cambiar la contraseña: " + e.getMessage());
      return "cambiar-clave";
    }
  }

}
