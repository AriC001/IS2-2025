package edu.egg.tinder.controladores;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import edu.egg.tinder.entidades.Mascota;
import edu.egg.tinder.entidades.Usuario;
import edu.egg.tinder.entidades.Voto;
import edu.egg.tinder.servicios.MascotaServicio;
import edu.egg.tinder.servicios.UsuarioServicio;
import edu.egg.tinder.servicios.VotoServicio;

/**
 * Controlador REST para APIs protegidas con JWT Access Token.
 * 
 * Todos estos endpoints requieren un Access Token válido de Auth0.
 * Para probarlo en Postman:
 * 1. Obtén un Access Token desde: POST https://dev-2qujqqri8c7ewao0.us.auth0.com/oauth/token
 * 2. Agrega el header: Authorization: Bearer <access_token>
 * 3. Haz la petición a cualquier endpoint
 * 
 * Endpoints disponibles:
 * - GET  /api/v1/usuarios/me          - Información del usuario autenticado
 * - GET  /api/v1/usuarios              - Listar todos los usuarios
 * - GET  /api/v1/usuarios/{id}         - Ver un usuario específico
 * 
 * - GET  /api/v1/mascotas              - Listar todas las mascotas activas
 * - GET  /api/v1/mascotas/{id}         - Ver una mascota específica
 * - GET  /api/v1/mascotas/usuario/{id} - Mascotas de un usuario
 * 
 * - GET  /api/v1/votos                 - Listar todos los votos
 * - GET  /api/v1/votos/mascota/{id}    - Votos de una mascota
 */
@RestController
@RequestMapping("/api/v1")
public class ApiRestController {

    @Autowired
    private UsuarioServicio usuarioServicio;

    @Autowired
    private MascotaServicio mascotaServicio;

    @Autowired
    private VotoServicio votoServicio;

    // ==================== ENDPOINTS DE USUARIOS ====================

    /**
     * GET /api/v1/usuarios/me
     * Obtiene la información del usuario autenticado desde el JWT
     */
    @GetMapping("/usuarios/me")
    public ResponseEntity<Map<String, Object>> getCurrentUser(@AuthenticationPrincipal Jwt jwt) {
        Map<String, Object> response = new HashMap<>();
        
        if (jwt == null) {
            response.put("error", "No hay Access Token JWT válido en la petición");
            response.put("message", "Debes enviar un Access Token en el header: Authorization: Bearer <access_token>");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

        // Información del JWT (Access Token)
        response.put("sub", jwt.getSubject());
        response.put("email", jwt.getClaimAsString("email"));
        response.put("name", jwt.getClaimAsString("name"));
        response.put("permissions", jwt.getClaimAsStringList("permissions"));
        response.put("exp", jwt.getExpiresAt());
        response.put("iat", jwt.getIssuedAt());
        response.put("aud", jwt.getAudience());
        
        return ResponseEntity.ok(response);
    }

    /**
     * GET /api/v1/usuarios/{id}
     * Obtiene un usuario específico por ID
     */
    @GetMapping("/usuarios/{id}")
    public ResponseEntity<Map<String, Object>> obtenerUsuario(
            @PathVariable Long id,
            @AuthenticationPrincipal Jwt jwt) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            Usuario usuario = usuarioServicio.findById(id);
            response.put("success", true);
            response.put("usuario", usuario);
            response.put("authenticated_as", jwt.getClaimAsString("email"));
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    /**
     * GET /api/v1/usuarios/email/{email}
     * Busca un usuario por email
     */
    @GetMapping("/usuarios/email/{email}")
    public ResponseEntity<Map<String, Object>> obtenerUsuarioPorEmail(
            @PathVariable String email,
            @AuthenticationPrincipal Jwt jwt) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            Usuario usuario = usuarioServicio.buscarUsuarioPorEmail(email);
            response.put("success", true);
            response.put("usuario", usuario);
            response.put("authenticated_as", jwt.getClaimAsString("email"));
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    // ==================== ENDPOINTS DE MASCOTAS ====================

    /**
     * GET /api/v1/mascotas
     * Lista todas las mascotas activas del sistema
     */
    @GetMapping("/mascotas")
    public ResponseEntity<Map<String, Object>> listarMascotas(@AuthenticationPrincipal Jwt jwt) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            List<Mascota> mascotas = mascotaServicio.listarAllMascotas();
            response.put("success", true);
            response.put("total", mascotas.size());
            response.put("mascotas", mascotas);
            response.put("authenticated_as", jwt.getClaimAsString("email"));
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * GET /api/v1/mascotas/{id}
     * Obtiene una mascota específica por ID
     */
    @GetMapping("/mascotas/{id}")
    public ResponseEntity<Map<String, Object>> obtenerMascota(
            @PathVariable Long id,
            @AuthenticationPrincipal Jwt jwt) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            Mascota mascota = mascotaServicio.buscarMascota(id);
            response.put("success", true);
            response.put("mascota", mascota);
            response.put("authenticated_as", jwt.getClaimAsString("email"));
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    /**
     * GET /api/v1/mascotas/usuario/{id}
     * Lista todas las mascotas de un usuario específico
     */
    @GetMapping("/mascotas/usuario/{id}")
    public ResponseEntity<Map<String, Object>> listarMascotasPorUsuario(
            @PathVariable Long id,
            @AuthenticationPrincipal Jwt jwt) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            List<Mascota> mascotas = mascotaServicio.listarMascotas(id);
            response.put("success", true);
            response.put("total", mascotas.size());
            response.put("usuario_id", id);
            response.put("mascotas", mascotas);
            response.put("authenticated_as", jwt.getClaimAsString("email"));
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * GET /api/v1/mascotas/{id}/votos
     * Lista todos los votos recibidos por una mascota específica
     */
    @GetMapping("/mascotas/{id}/votos")
    public ResponseEntity<Map<String, Object>> listarVotosDeMascota(
            @PathVariable Long id,
            @AuthenticationPrincipal Jwt jwt) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            Mascota mascota = mascotaServicio.buscarMascota(id);
            List<Voto> votos = mascotaServicio.votosDe(mascota);
            response.put("success", true);
            response.put("total", votos.size());
            response.put("mascota_id", id);
            response.put("mascota_nombre", mascota.getNombre());
            response.put("votos", votos);
            response.put("authenticated_as", jwt.getClaimAsString("email"));
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // ==================== ENDPOINTS DE VOTOS ====================

    /**
     * POST /api/v1/votos
     * Crear un nuevo voto (mascota1 vota a mascota2)
     * Body JSON: { "idUsuario": 1, "idMascota1": 1, "idMascota2": 2 }
     */
    @PostMapping("/votos")
    public ResponseEntity<Map<String, Object>> crearVoto(
            @RequestBody Map<String, Long> requestBody,
            @AuthenticationPrincipal Jwt jwt) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            Long idUsuario = requestBody.get("idUsuario");
            Long idMascota1 = requestBody.get("idMascota1");
            Long idMascota2 = requestBody.get("idMascota2");
            
            votoServicio.votar(idUsuario, idMascota1, idMascota2);
            response.put("success", true);
            response.put("message", "Voto registrado exitosamente");
            response.put("authenticated_as", jwt.getClaimAsString("email"));
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    /**
     * POST /api/v1/votos/{id}/responder
     * Responder a un voto recibido
     */
    @PostMapping("/votos/{id}/responder")
    public ResponseEntity<Map<String, Object>> responderVoto(
            @PathVariable Long id,
            @RequestBody Map<String, Long> requestBody,
            @AuthenticationPrincipal Jwt jwt) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            Long idUsuario = requestBody.get("idUsuario");
            votoServicio.responder(idUsuario, id);
            response.put("success", true);
            response.put("message", "Voto respondido exitosamente");
            response.put("authenticated_as", jwt.getClaimAsString("email"));
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    // ==================== ENDPOINT DE HEALTH CHECK ====================

    /**
     * GET /api/v1/health
     * Verifica que la API está funcionando y el token es válido
     */
    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> healthCheck(@AuthenticationPrincipal Jwt jwt) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "UP");
        response.put("message", "API está funcionando correctamente");
        response.put("authenticated", jwt != null);
        if (jwt != null) {
            response.put("authenticated_as", jwt.getClaimAsString("email"));
            response.put("token_expires_at", jwt.getExpiresAt());
        }
        return ResponseEntity.ok(response);
    }
}
