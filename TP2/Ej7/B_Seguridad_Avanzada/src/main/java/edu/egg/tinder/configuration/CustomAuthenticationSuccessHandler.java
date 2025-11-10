package edu.egg.tinder.configuration;

import java.io.IOException;
import java.util.UUID;

import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import edu.egg.tinder.entidades.Usuario;
import edu.egg.tinder.errores.ErrorServicio;
import edu.egg.tinder.servicios.UsuarioServicio;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * Manejador de éxito de autenticación que soporta:
 * - Login tradicional (usuario/contraseña de BD)
 * - Login OAuth2 con Auth0 (creación automática de usuario)
 */
@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final UsuarioServicio usuarioService;

    public CustomAuthenticationSuccessHandler(@Lazy UsuarioServicio usuarioService) {
        this.usuarioService = usuarioService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {
        
        String email = extraerEmail(authentication);
        boolean isOAuth2 = authentication.getClass().getName().contains("OAuth2");
        
        try {
            Usuario usuario = buscarOCrearUsuario(authentication, email, isOAuth2);
            guardarEnSesion(request, usuario);
            response.sendRedirect("/inicio");
        } catch (ErrorServicio e) {
            response.sendRedirect("/login?error=true");
        }
    }

    private String extraerEmail(Authentication authentication) {
        String email = authentication.getName();
        
        if (authentication.getPrincipal() instanceof OidcUser oidcUser) {
            if (oidcUser.getEmail() != null) {
                email = oidcUser.getEmail();
            }
        }
        
        return email;
    }

    private Usuario buscarOCrearUsuario(Authentication authentication, String email, boolean isOAuth2) 
            throws ErrorServicio {
        try {
            return usuarioService.buscarUsuarioPorEmail(email);
        } catch (ErrorServicio e) {
            if (isOAuth2) {
                return crearUsuarioDesdeOAuth2(authentication, email);
            }
            throw e;
        }
    }

    private Usuario crearUsuarioDesdeOAuth2(Authentication authentication, String email) throws ErrorServicio {
        OidcUser oidcUser = (OidcUser) authentication.getPrincipal();
        
        String nombre = obtenerNombre(oidcUser, email);
        String apellido = obtenerApellido(oidcUser);
        String password = UUID.randomUUID().toString();
        
        return usuarioService.registrar(null, nombre, apellido, email, password, password);
    }

    private String obtenerNombre(OidcUser oidcUser, String email) {
        if (oidcUser.getGivenName() != null && !oidcUser.getGivenName().isEmpty()) {
            return oidcUser.getGivenName();
        }
        
        if (oidcUser.getFullName() != null && !oidcUser.getFullName().isEmpty()) {
            return oidcUser.getFullName().split(" ")[0];
        }
        
        return email.split("@")[0];
    }

    private String obtenerApellido(OidcUser oidcUser) {
        if (oidcUser.getFamilyName() != null && !oidcUser.getFamilyName().isEmpty()) {
            return oidcUser.getFamilyName();
        }
        
        if (oidcUser.getFullName() != null && !oidcUser.getFullName().isEmpty()) {
            String[] partes = oidcUser.getFullName().split(" ", 2);
            if (partes.length > 1) {
                return partes[1];
            }
        }
        
        return "OAuth2";
    }

    private void guardarEnSesion(HttpServletRequest request, Usuario usuario) {
        HttpSession session = request.getSession();
        session.setAttribute("usuariosession", usuario);
    }
}
