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
      
      // Guardar información en la sesión
      session.setAttribute("token", authResponse.getToken());
      session.setAttribute("usuarioId", authResponse.getId());
      session.setAttribute("nombreUsuario", authResponse.getNombreUsuario());
      session.setAttribute("rol", authResponse.getRol().toString());
      
      logger.info("✅ Login exitoso para usuario: {} con rol: {}", nombreUsuario, authResponse.getRol());
      
      // Redirigir al dashboard principal (solo JEFE y ADMINISTRATIVO)
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

}
