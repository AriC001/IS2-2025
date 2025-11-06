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

@RestController
@RequestMapping("/api/auth")
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

}
