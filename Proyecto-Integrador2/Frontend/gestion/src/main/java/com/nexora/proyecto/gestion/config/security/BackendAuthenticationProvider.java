package com.nexora.proyecto.gestion.config.security;

import java.util.List;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.nexora.proyecto.gestion.business.logic.service.AuthService;
import com.nexora.proyecto.gestion.dto.AuthResponseDTO;
import com.nexora.proyecto.gestion.dto.LoginRequestDTO;

@Component
public class BackendAuthenticationProvider implements AuthenticationProvider {

    private final AuthService authService;

    public BackendAuthenticationProvider(AuthService authService) {
        this.authService = authService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = authentication.getCredentials() == null ? "" : authentication.getCredentials().toString();

        try {
            LoginRequestDTO req = LoginRequestDTO.builder()
                    .nombreUsuario(username)
                    .clave(password)
                    .build();

            AuthResponseDTO resp = authService.login(req);

            // Build authorities from the role returned by backend
            GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + (resp.getRol() != null ? resp.getRol().name() : "CLIENTE"));
            List<GrantedAuthority> authorities = List.of(authority);

            UserDetails user = User.withUsername(resp.getNombreUsuario() != null ? resp.getNombreUsuario() : username)
                    .password("")
                    .authorities(authorities)
                    .build();

            UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(user, null, authorities);
            // store auth response in details so handlers can access token/id if needed
            auth.setDetails(resp);
            return auth;
        } catch (Exception e) {
            throw new BadCredentialsException("Credenciales inválidas", e);
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
