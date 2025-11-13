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
import nexora.proyectointegrador2.utils.security.GitHubTokenService;
import nexora.proyectointegrador2.business.persistence.repository.UsuarioRepository;
import nexora.proyectointegrador2.business.domain.entity.Usuario;

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
        private final GitHubTokenService gitHubTokenService;

        private final UsuarioRepository usuarioRepository;

        public JwtAuthenticationFilter(JwtUtil jwtUtil, GitHubTokenService gitHubTokenService, UsuarioRepository usuarioRepository) {
            this.jwtUtil = jwtUtil;
            this.gitHubTokenService = gitHubTokenService;
            this.usuarioRepository = usuarioRepository;
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

                // 4. Primero intentamos manejarlo como JWT (token emitido por nuestra app)
                String username = null;
                boolean jwtValid = false;
                boolean isProviderToken = false;
                try {
                    username = jwtUtil.extractUsername(token);
                    // 5. Validar el token (firma, expiración, formato)
                    jwtValid = jwtUtil.validateToken(token, username);
                } catch (Exception e) {
                    // No es un JWT válido — puede ser un token de proveedor (ej. GitHub)
                    username = null;
                    jwtValid = false;
                }

                if (!jwtValid) {
                    // Intentar validar como token de GitHub (u otro proveedor)
                    String githubLogin = gitHubTokenService.validateTokenAndGetLogin(token);
                    if (githubLogin != null) {
                        // Autenticamos al usuario usando el login de GitHub
                        // Use the provider login directly (no prefix) so it matches local Usuario.nombreUsuario
                        username = githubLogin;
                        jwtValid = true;
                        isProviderToken = true;
                        log.debug("Token de proveedor válido (GitHub) para: {}", githubLogin);
                        // Try to resolve a local Usuario with this login so downstream logic has an id/role
                        try {
                            java.util.Optional<Usuario> opt = usuarioRepository.findByNombreUsuarioAndEliminadoFalse(githubLogin);
                            if (opt.isPresent()) {
                                Usuario u = opt.get();
                                // overwrite username to the exact stored nombreUsuario (already githubLogin)
                                username = u.getNombreUsuario();
                                // store usuarioId and role later when building authentication
                                log.debug("GitHub login linked to local Usuario id={} rol={}", u.getId(), u.getRol());
                            } else {
                                log.debug("No se encontró Usuario local para GitHub login={}", githubLogin);
                            }
                        } catch (Exception ex) {
                            log.warn("Error buscando Usuario local para login de proveedor: {}", ex.getMessage());
                        }
                    }
                }

                if (jwtValid) {
                    // 6. Extraer información adicional del usuario desde el token
                    String rol;
                    String usuarioId = null;
                    if (isProviderToken) {
                        // Token de proveedor (GitHub): try to extract role/id from local user if available
                        // If not available, default to USER
                        try {
                            java.util.Optional<Usuario> opt = usuarioRepository.findByNombreUsuarioAndEliminadoFalse(username);
                            if (opt.isPresent()) {
                                Usuario u = opt.get();
                                rol = u.getRol() != null ? u.getRol().name() : "USER";
                                usuarioId = u.getId();
                            } else {
                                rol = "USER";
                            }
                        } catch (Exception ex) {
                            rol = "USER";
                        }
                    } else {
                        rol = jwtUtil.extractRol(token);
                        usuarioId = jwtUtil.extractUsuarioId(token);
                    }

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
                    log.warn("Token inválido o expirado en petición a: {}", request.getRequestURI());
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
        
        // No filtrar endpoints públicos
        boolean shouldSkip = path != null && (
            // Endpoints de autenticación
            path.startsWith("/api/v1/auth/") || 
            path.equals("/api/v1/auth/login") ||
            path.equals("/api/v1/auth/register/cliente") ||
            
            // Frontend: formularios de auth
            path.startsWith("/auth/registro") ||
            path.startsWith("/auth/login") ||
             path.startsWith("/api/v1/weather") ||
            // Endpoints públicos para el formulario de registro
            path.startsWith("/api/v1/nacionalidades") ||
            path.startsWith("/api/v1/localidades") ||

            path.startsWith("/favicon.ico") ||
            path.startsWith("/webhook") ||
            path.equals("/webhook")
        );
        
        if (shouldSkip) {
            log.debug("✅ Saltando filtro JWT para endpoint público: {}", path);
        } else {
            log.debug("🔍 Filtro JWT se ejecutará para: {}", path);
        }
        return shouldSkip;
    }
}
