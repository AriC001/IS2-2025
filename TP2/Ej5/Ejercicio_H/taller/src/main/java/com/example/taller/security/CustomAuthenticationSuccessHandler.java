package com.example.taller.security;

import com.example.taller.adapter.UsuarioAdapter;
import com.example.taller.dto.UsuarioSafeDTO;
import com.example.taller.entity.Usuario;
import com.example.taller.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

/**
 * AuthenticationSuccessHandler that stores a safe DTO of the authenticated user in session
 * and then delegates to the default SavedRequestAwareAuthenticationSuccessHandler.
 */
@Component
public class CustomAuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws ServletException, IOException {

        try {
            if (authentication != null && StringUtils.hasText(authentication.getName())) {
                String username = authentication.getName();
                Usuario usuario = usuarioRepository.findByNombreUsuario(username).orElse(null);
                if (usuario != null) {
                    UsuarioSafeDTO safe = UsuarioAdapter.toSafeDTO(usuario);
                    HttpSession session = request.getSession(true);
                    session.setAttribute("usuariosession", safe);
                    // set a flag if this usuario has an associated mecanico entity
                    boolean isMecanico = usuario.getMecanico() != null;
                    session.setAttribute("ismecanico", isMecanico);
                } else {
                    // Guardar indicador vacío para evitar NPEs en las vistas si se espera atributo
                    HttpSession session = request.getSession(true);
                    session.removeAttribute("usuariosession");
                }
            }
        } catch (Exception ex) {
            // Log simple info to console to help debugging in dev. Do not prevent redirect.
            System.err.println("[CustomAuthenticationSuccessHandler] Error al cargar usuario para sesión: " + ex.getMessage());
            ex.printStackTrace();
        }

        // Delegate to the default behaviour (redirect to saved request or defaultTargetUrl)
        super.onAuthenticationSuccess(request, response, authentication);
    }
}
