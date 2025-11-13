package nexora.proyectointegrador2.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import nexora.proyectointegrador2.business.logic.service.RecordatorioService;

/**
 * Controller de prueba para demostrar el funcionamiento del filtro JWT
 * Todos los endpoints aquí requieren un token JWT válido
 */
@RestController
@RequestMapping("/api/test")
public class TestController {

    @Autowired(required = false)
    private RecordatorioService recordatorioService;

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

    /**
     * Endpoint de prueba para ejecutar manualmente el proceso de recordatorios.
     * Útil para testing sin esperar a las 9:00 AM.
     * 
     * Ejemplo de uso:
     * POST http://localhost:9000/api/test/recordatorios/enviar
     */
    @PostMapping("/recordatorios/enviar")
    public ResponseEntity<Map<String, Object>> enviarRecordatorios() {
        Map<String, Object> response = new HashMap<>();
        
        if (recordatorioService == null) {
            response.put("status", "error");
            response.put("message", "RecordatorioService no está disponible");
            return ResponseEntity.status(500).body(response);
        }
        
        try {
            recordatorioService.enviarRecordatoriosDevolucionManual();
            response.put("status", "success");
            response.put("message", "Proceso de recordatorios ejecutado. Revisa los logs para ver los detalles.");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", "Error al ejecutar recordatorios: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }
}
