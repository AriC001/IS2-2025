package edu.egg.tinder.controladores;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import edu.egg.tinder.servicios.Auth0TokenService;

/**
 * Controlador para obtener Access Tokens de Auth0 automáticamente.
 * Útil para desarrollo y testing.
 */
@RestController
@RequestMapping("/auth0")
public class Auth0TokenController {

    @Autowired
    private Auth0TokenService tokenService;

    /**
     * Obtiene un Access Token válido de Auth0.
     * Si hay uno en caché y no ha expirado, lo devuelve.
     * Si no, solicita uno nuevo automáticamente.
     * 
     * GET http://localhost:9000/auth0/token/get
     */
    @GetMapping("/token/get")
    public ResponseEntity<Map<String, Object>> obtenerToken() {
        Map<String, Object> response = new HashMap<>();
        
        try {
            String accessToken = tokenService.obtenerAccessToken();
            
            response.put("success", true);
            response.put("access_token", accessToken);
            response.put("expires", tokenService.getTokenExpiryTime());
            response.put("token_type", "Bearer");

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("error", e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }

    /**
     * Invalida el token actual en caché.
     * El próximo uso solicitará un nuevo token.
     * 
     * POST http://localhost:9000/auth0/token/invalidar
     */
    @GetMapping("/token/invalidar")
    public ResponseEntity<Map<String, Object>> invalidarToken() {
        Map<String, Object> response = new HashMap<>();
        
        tokenService.invalidarToken();
        
        response.put("success", true);
        response.put("message", "Token invalidado. El próximo uso solicitará uno nuevo.");
        
        return ResponseEntity.ok(response);
    }
}
