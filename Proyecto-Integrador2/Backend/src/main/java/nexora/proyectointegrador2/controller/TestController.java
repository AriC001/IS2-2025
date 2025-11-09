package nexora.proyectointegrador2.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller de prueba para demostrar el funcionamiento del filtro JWT
 * Todos los endpoints aquí requieren un token JWT válido
 */
@RestController
@RequestMapping("/api/test")
public class TestController {

    /**
     * Endpoint público de prueba (sin autenticación)
     * Nota: Este endpoint está bajo /api/test, por lo que SÍ requiere autenticación
     * según la configuración de SecurityConfig
     */
    @GetMapping("/hello")
    public ResponseEntity<Map<String, Object>> hello() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        
        Map<String, Object> response = new HashMap<>();
        response.put("message", "¡Hola! Estás autenticado con JWT");
        response.put("username", auth.getName());
        response.put("authorities", auth.getAuthorities());
        response.put("authenticated", auth.isAuthenticated());
        
        return ResponseEntity.ok(response);
    }

    /**
     * Endpoint que devuelve información del usuario autenticado
     */
    @GetMapping("/me")
    public ResponseEntity<Map<String, Object>> getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        
        Map<String, Object> response = new HashMap<>();
        response.put("username", auth.getName());
        response.put("roles", auth.getAuthorities());
        response.put("isAuthenticated", auth.isAuthenticated());
        
        return ResponseEntity.ok(response);
    }

    /**
     * Endpoint de prueba que simula una operación protegida
     */
    @GetMapping("/protected")
    public ResponseEntity<Map<String, String>> protectedEndpoint() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        
        Map<String, String> response = new HashMap<>();
        response.put("message", "Este es un endpoint protegido por JWT");
        response.put("authenticatedUser", auth.getName());
        response.put("status", "success");
        
        return ResponseEntity.ok(response);
    }
}
