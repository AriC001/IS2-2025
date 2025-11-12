package com.nexora.proyectointegrador2.front_cliente.controller;

import java.time.LocalDate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.nexora.proyectointegrador2.front_cliente.business.logic.service.AuthService;
import com.nexora.proyectointegrador2.front_cliente.business.logic.service.ClienteDataService;
import com.nexora.proyectointegrador2.front_cliente.dto.AuthResponseDTO;
import com.nexora.proyectointegrador2.front_cliente.dto.LoginRequestDTO;
import com.nexora.proyectointegrador2.front_cliente.dto.RegisterRequestDTO;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/auth")
public class AuthController {

  private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
  
  private final AuthService authService;
  private final ClienteDataService clienteDataService;

  public AuthController(AuthService authService, ClienteDataService clienteDataService) {
    this.authService = authService;
    this.clienteDataService = clienteDataService;
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
      
      // Redirigir al dashboard principal
      return "redirect:/views/home";
      
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

  // ==================== REGISTRO DE CLIENTE ====================

  @GetMapping("/registro")
  public String mostrarFormularioRegistro(Model model) {
    try {
      // Cargar datos necesarios para los selects
      model.addAttribute("nacionalidades", clienteDataService.obtenerNacionalidades());
      model.addAttribute("localidades", clienteDataService.obtenerLocalidades());
      return "registro";
    } catch (Exception e) {
      logger.error("Error al cargar formulario de registro: {}", e.getMessage());
      model.addAttribute("error", "Error al cargar el formulario");
      return "registro";
    }
  }

  @PostMapping("/registro")
  public String registrarCliente(
      // Datos personales
      @RequestParam String nombre,
      @RequestParam String apellido,
      @RequestParam LocalDate fechaNacimiento,
      @RequestParam String email,
      @RequestParam String tipoDocumento,
      @RequestParam String numeroDocumento,
      @RequestParam String telefono,
      @RequestParam String nacionalidadId,
      
      // Dirección
      @RequestParam String calle,
      @RequestParam String numero,
      @RequestParam(required = false) String barrio,
      @RequestParam String localidadId,
      @RequestParam(required = false) String manzanaPiso,
      @RequestParam(required = false) String casaDepartamento,
      @RequestParam(required = false) String referencia,
      @RequestParam(required = false) String direccionEstadia,
      
      // Datos de acceso
      @RequestParam String nombreUsuario,
      @RequestParam String clave,
      @RequestParam String confirmarClave,
      
      HttpSession session,
      RedirectAttributes redirectAttributes,
      Model model) {

    try {
      // Validar que las contraseñas coincidan
      if (!clave.equals(confirmarClave)) {
        model.addAttribute("error", "Las contraseñas no coinciden");
        cargarDatosFormulario(model);
        return "registro";
      }

      // Validar edad mínima (18 años)
      if (fechaNacimiento.isAfter(LocalDate.now().minusYears(18))) {
        model.addAttribute("error", "Debes ser mayor de 18 años para registrarte");
        cargarDatosFormulario(model);
        return "registro";
      }

      // Crear DTO con todos los datos
      RegisterRequestDTO registerRequest = RegisterRequestDTO.builder()
          .nombreUsuario(nombreUsuario)
          .clave(clave)
          .nombre(nombre)
          .apellido(apellido)
          .fechaNacimiento(fechaNacimiento)
          .email(email)
          .tipoDocumento(tipoDocumento)
          .numeroDocumento(numeroDocumento)
          .telefono(telefono)
          .nacionalidadId(nacionalidadId)
          .calle(calle)
          .numero(numero)
          .barrio(barrio)
          .localidadId(localidadId)
          .manzanaPiso(manzanaPiso)
          .casaDepartamento(casaDepartamento)
          .referencia(referencia)
          .direccionEstadia(direccionEstadia)
          .build();

      logger.info("Intentando registrar nuevo cliente: {}", nombreUsuario);

      // Llamar al servicio de registro
      AuthResponseDTO authResponse = authService.register(registerRequest);

      logger.info("✅ Registro exitoso para cliente: {}", nombreUsuario);

      // Redirigir al login (sin auto-login)
      redirectAttributes.addFlashAttribute("success", "¡Registro exitoso! Por favor inicia sesión");
      return "redirect:/auth/login";

    } catch (Exception e) {
      logger.error("Error en registro: {}", e.getMessage());
      model.addAttribute("error", e.getMessage());
      cargarDatosFormulario(model);
      return "registro";
    }
  }

  private void cargarDatosFormulario(Model model) {
    try {
      model.addAttribute("nacionalidades", clienteDataService.obtenerNacionalidades());
      model.addAttribute("localidades", clienteDataService.obtenerLocalidades());
    } catch (Exception e) {
      logger.error("Error al cargar datos del formulario: {}", e.getMessage());
    }
  }

}