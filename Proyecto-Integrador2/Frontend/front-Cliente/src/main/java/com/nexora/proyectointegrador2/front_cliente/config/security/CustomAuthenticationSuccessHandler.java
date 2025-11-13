package  com.nexora.proyectointegrador2.front_cliente.config.security;

import java.io.IOException;

import org.springframework.context.annotation.Lazy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import java.util.Optional;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.nexora.proyectointegrador2.front_cliente.dto.UsuarioDTO;
import com.nexora.proyectointegrador2.front_cliente.business.logic.service.UsuarioService;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final UsuarioService usuarioService;
  @Autowired(required = false)
  private OAuth2AuthorizedClientService authorizedClientService;

    public CustomAuthenticationSuccessHandler(@Lazy UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {

      // Obtener el nombreUsuario del usuario autenticado
      String nombreUsuario = authentication.getName();
      System.out.println("CustomAuthenticationSuccessHandler: nombreUsuario=" + nombreUsuario);
      System.out.println("CustomAuthenticationSuccessHandler: authentication.getPrincipal()=" + authentication.getPrincipal().getClass().getName());

      try {
        // Preferir usar los detalles devueltos por el Authentication (provistos por BackendAuthenticationProvider)
        Object details = authentication.getDetails();
        UsuarioDTO usuario = null;

        if (details instanceof com.nexora.proyectointegrador2.front_cliente.dto.AuthResponseDTO) {
          com.nexora.proyectointegrador2.front_cliente.dto.AuthResponseDTO authResp = (com.nexora.proyectointegrador2.front_cliente.dto.AuthResponseDTO) details;
      usuario = new UsuarioDTO();
      usuario.setId(authResp.getId());
      usuario.setNombreUsuario(authResp.getNombreUsuario());
      usuario.setRol(authResp.getRol());
          // Store token in session as well
          HttpSession session = request.getSession();
          session.setAttribute("token", authResp.getToken());
          session.setAttribute("usuarioId", authResp.getId());
          session.setAttribute("nombreUsuario", authResp.getNombreUsuario());
          session.setAttribute("rol", authResp.getRol() != null ? authResp.getRol().toString() : null);

          System.out.println("Usuario obtenido desde AuthResponseDTO: " + authResp.getNombreUsuario() + " (rol: " + authResp.getRol() + ")");
        } else {
          // Fallback: intentar obtenerlo vía UsuarioService (si el backend permite acceso)
          Optional<UsuarioDTO> usuarioOpt = usuarioService.findByUsername(nombreUsuario);
          if (usuarioOpt.isEmpty()) {
            System.err.println("ERROR: No se encontró usuario en BD con nombre: " + nombreUsuario);
            response.sendRedirect("/auth/login?error=usuario_no_encontrado");
            return;
          }
          usuario = usuarioOpt.get();
          System.out.println("Usuario encontrado en BD: " + usuario.getNombreUsuario() + " (rol: " + usuario.getRol() + ")");
          HttpSession session = request.getSession();
          session.setAttribute("usuariosession", usuario);
        }
        // Si el método de autenticación fue OAuth2 y tenemos acceso al OAuth2AuthorizedClientService,
        // almacenar el token de acceso OAuth2 en la sesión bajo la clave "token" para compatibilidad
        // con los controladores que verifican la presencia de "token" en sesión.
        if (authentication instanceof OAuth2AuthenticationToken && authorizedClientService != null) {
          try {
            OAuth2AuthenticationToken oauth2 = (OAuth2AuthenticationToken) authentication;
            OAuth2AuthorizedClient client = authorizedClientService.loadAuthorizedClient(oauth2.getAuthorizedClientRegistrationId(), oauth2.getName());
            if (client != null && client.getAccessToken() != null && client.getAccessToken().getTokenValue() != null) {
              String oauth2Token = client.getAccessToken().getTokenValue();
              HttpSession session = request.getSession();
              session.setAttribute("token", oauth2Token);
              System.out.println("OAuth2 access token guardado en sesión para usuario: " + oauth2.getName());
            }
          } catch (Exception e) {
            System.err.println("No se pudo almacenar OAuth2 token en sesión: " + e.getMessage());
          }
        }
        // Si no lo habíamos guardado en sesión aún (caso donde creamos desde AuthResponseDTO), guardarlo
        HttpSession session = request.getSession();
        if (session.getAttribute("usuariosession") == null && usuario != null) {
          session.setAttribute("usuariosession", usuario);
          System.out.println("Usuario guardado en sesión HTTP");
        }

        // Redirigir según rol: CLIENTE -> /home, ADMIN -> /portal/admin, EMPLEADO -> /portal/empleado, SOCIO -> /portal/socio
        String targetUrl = "/home"; // Por defecto redirigir a /home para clientes
        if (authentication.getAuthorities() != null) {
          boolean isAdmin = authentication.getAuthorities().stream()
              .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
          boolean isEmpleado = authentication.getAuthorities().stream()
              .anyMatch(a -> a.getAuthority().equals("ROLE_EMPLEADO"));
          boolean isSocio = authentication.getAuthorities().stream()
              .anyMatch(a -> a.getAuthority().equals("ROLE_SOCIO"));
          boolean isCliente = authentication.getAuthorities().stream()
              .anyMatch(a -> a.getAuthority().equals("ROLE_CLIENTE"));

          if (isAdmin) {
            targetUrl = "/portal/admin";
          } else if (isEmpleado) {
            targetUrl = "/portal/empleado";
          } else if (isSocio) {
            targetUrl = "/portal/socio";
          } else if (isCliente) {
            targetUrl = "/home";
          }
        } else {
          // Si no hay authorities pero hay un rol en la sesión, verificar el rol
          Object rolAttr = session.getAttribute("rol");
          if (rolAttr != null) {
            String rol = rolAttr.toString();
            if ("CLIENTE".equals(rol)) {
              targetUrl = "/home";
            }
          }
        }

        System.out.println("Redirigiendo a: " + targetUrl);
        response.sendRedirect(targetUrl);
        
      } catch (Exception e) {
        System.err.println("Exception en CustomAuthenticationSuccessHandler: " + e.getMessage());
        e.printStackTrace();
        response.sendRedirect("/auth/login?error");
      }
    }
}
