package nexora.proyectointegrador2.configs;

import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nexora.proyectointegrador2.utils.security.JwtUtil;

/**
 * Filtro que intercepta cada petición HTTP y valida el token JWT
 * Si el token es válido, autentica al usuario en el contexto de Spring Security
 * 
 * Este filtro se ejecuta ANTES de que la petición llegue a los controllers
 * permitiendo proteger automáticamente todos los endpoints de la API
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private static final Logger log = LoggerFactory.getLogger(JwtAuthenticationFilter.class);
    
  private final JwtUtil jwtUtil;

  public JwtAuthenticationFilter(JwtUtil jwtUtil) {
    this.jwtUtil = jwtUtil;
  }

  @Override
  protected void doFilterInternal(
          HttpServletRequest request,
          HttpServletResponse response,
          FilterChain filterChain) throws ServletException, IOException {

        try {
            // 1. Extraer el header Authorization de la petición
            String authHeader = request.getHeader("Authorization");

            // 2. Verificar si el header existe y tiene el formato correcto "Bearer <token>"
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                
                // 3. Extraer el token JWT (quitar el prefijo "Bearer ")
                String token = authHeader.substring(7);
                
                log.debug("Token JWT detectado en la petición: {}", request.getRequestURI());

                // 4. Primero extraemos el username para poder validar
                String username = jwtUtil.extractUsername(token);
                
                // 5. Validar el token (firma, expiración, formato)
                if (jwtUtil.validateToken(token, username)) {
                    
                    // 6. Extraer información adicional del usuario desde el token
                    String rol = jwtUtil.extractRol(token);
                    String usuarioId = jwtUtil.extractUsuarioId(token);

                    log.debug("Token válido para usuario: {} (ID: {}, Rol: {})", username, usuarioId, rol);

                    // 7. Crear objeto de autenticación de Spring Security
                    // El rol se convierte a ROLE_JEFE, ROLE_ADMINISTRATIVO, etc.
                    UsernamePasswordAuthenticationToken authentication = 
                        new UsernamePasswordAuthenticationToken(
                            username,
                            null, // No necesitamos la contraseña aquí
                            List.of(new SimpleGrantedAuthority("ROLE_" + rol))
                        );

                    // 8. Establecer la autenticación en el contexto de seguridad
                    // Esto permite que Spring Security sepa que el usuario está autenticado
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    
                    log.debug("Usuario autenticado exitosamente en el contexto de seguridad");
                    
                } else {
                    log.warn("Token JWT inválido o expirado en petición a: {}", request.getRequestURI());
                }
            } else {
                log.debug("No se encontró token JWT en la petición a: {}", request.getRequestURI());
            }
            
        } catch (Exception e) {
            log.error("Error procesando token JWT: {}", e.getMessage());
            // No lanzamos la excepción, simplemente no autenticamos al usuario
            // Spring Security se encargará de rechazar la petición si el endpoint requiere autenticación
        }

        // 9. Continuar con la cadena de filtros
        // Si el usuario fue autenticado, la petición llegará al controller
        // Si no, Spring Security la rechazará (si el endpoint está protegido)
        filterChain.doFilter(request, response);
    }

    /**
     * Podemos excluir ciertos endpoints del filtro si es necesario
     * Por ejemplo, endpoints públicos que no necesitan JWT
     */
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        // No filtrar endpoints de autenticación
        // The application exposes authentication endpoints under /api/v1/auth/ —
        // make sure the JWT filter does not run for them so login/register work
        boolean shouldSkip = path != null && (path.startsWith("/api/v1/auth/") || path.equals("/api/v1/auth/login"));
        if (shouldSkip) {
            log.debug("✅ Saltando filtro JWT para endpoint público: {}", path);
        } else {
            log.debug("🔍 Filtro JWT se ejecutará para: {}", path);
        }
        return shouldSkip;
    }
}
