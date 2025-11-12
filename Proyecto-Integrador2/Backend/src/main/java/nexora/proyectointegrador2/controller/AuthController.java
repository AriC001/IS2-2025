package nexora.proyectointegrador2.controller;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import nexora.proyectointegrador2.business.logic.service.AuthService;
import nexora.proyectointegrador2.utils.dto.AuthResponseDTO;
import nexora.proyectointegrador2.utils.dto.LoginRequestDTO;
import nexora.proyectointegrador2.utils.dto.RegisterRequestDTO;
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

  private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

  private final AuthService authService;

  public AuthController(AuthService authService) {
    this.authService = authService;
  }

  @PostMapping("/login")
  public ResponseEntity<?> login(@Valid @RequestBody LoginRequestDTO loginRequest) {
    try {
      logger.info("Petición de login recibida para usuario: {}", loginRequest.getNombreUsuario());
      AuthResponseDTO response = authService.login(loginRequest);
      return ResponseEntity.ok(response);
    } catch (Exception e) {
      logger.error("Error en login: {}", e.getMessage());
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
          .body(Map.of("error", e.getMessage()));
    }
  }
  @PostMapping("/registro")
  public ResponseEntity<?> register(@Valid @RequestBody RegisterRequestDTO registerRequest) {
    logger.info("Intentando registrar nuevo cliente: {}", registerRequest.getNombreUsuario());

    try {
      authService.register(registerRequest);
      
      return ResponseEntity.status(HttpStatus.CREATED)
          .body(Map.of("message", "Registro completado exitosamente."));

    } catch (Exception e) {
      logger.error("Error en registro para el usuario {}: {}", registerRequest.getNombreUsuario(), e.getMessage());

      String errorMessage = e.getMessage();
      
      // 1. Manejo de conflicto (Usuario ya existe): Buscamos la cadena de texto específica.
      if (errorMessage != null && (errorMessage.contains("ya está registrado") || errorMessage.contains("ya en uso"))) { 
          return ResponseEntity.status(HttpStatus.CONFLICT) // 409 Conflict
                  .body(Map.of("error", errorMessage));
      }
      
      // 2. Manejo de error interno: Cualquier otra excepción es un error 500.
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body(Map.of("error", "Ha ocurrido un error inesperado en el servidor."));
    }
  }
}
