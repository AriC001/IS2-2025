package com.nexora.proyecto.gestion.config.security;

import java.util.*;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.nexora.proyecto.gestion.business.logic.service.UsuarioService;
import com.nexora.proyecto.gestion.dto.UsuarioDTO;
import com.nexora.proyecto.gestion.dto.enums.RolUsuario;

/**
 * Servicio personalizado de OAuth2 que crea automáticamente un Usuario con rol SOCIO
 * la primera vez que alguien se loguea con GitHub.
 */
@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final DefaultOAuth2UserService delegate = new DefaultOAuth2UserService();

    private final UsuarioService usuarioService;

    public CustomOAuth2UserService(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        // 1. Cargar el usuario OAuth2 desde GitHub
        OAuth2User oauth2User = delegate.loadUser(userRequest);
        Map<String, Object> attributes = oauth2User.getAttributes();

        // 2. Extraer email o login como identificador
        String email = (String) attributes.get("email");
        if (email == null || email.isEmpty()) {
            // Fallback al username de GitHub si no hay email público
            email = (String) attributes.get("login");
        }

        if (email == null || email.isEmpty()) {
            throw new OAuth2AuthenticationException("No se pudo obtener email o login de GitHub");
        }

        // 3. Buscar o crear usuario local (usando la API/DAO a través de UsuarioService)
        UsuarioDTO usuario = null;
        try {
            Optional<UsuarioDTO> existing = usuarioService.findByUsername(email);

            if (existing.isPresent()) {
                usuario = existing.get();
                System.out.println("Usuario OAuth2 ya existe: " + email);
            } else {
                // Usuario no existe - crear Usuario (rol mapeado a RolUsuario.CLIENTE por defecto)
                System.out.println("Creando nuevo usuario para OAuth2: " + email);
                String randomPwd = UUID.randomUUID().toString().substring(0, 8);

                UsuarioDTO nuevo = UsuarioDTO.builder()
                        .nombreUsuario(email)
                        .clave(randomPwd)
                        .rol(RolUsuario.CLIENTE)
                        .build();

                usuario = usuarioService.create(nuevo);
                System.out.println("Usuario creado vía API: " + email + " (id: " + (usuario != null ? usuario.getId() : "N/A") + ")");
            }
        } catch (Exception e) {
            System.err.println("Error creando usuario OAuth2 vía API: " + e.getMessage());
            e.printStackTrace();
            throw new OAuth2AuthenticationException("Error al crear/obtener usuario local: " + e.getMessage());
        }

        // 4. Construir authorities según el rol del usuario (prefijo ROLE_)
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        if (usuario != null && usuario.getRol() != null) {
            authorities.add(new SimpleGrantedAuthority("ROLE_" + usuario.getRol().name()));
        } else {
            // fallback conservador
            authorities.add(new SimpleGrantedAuthority("ROLE_CLIENTE"));
        }

        // 5. Construir attributes modificados con el email/login como name principal
        Map<String, Object> modifiedAttributes = new HashMap<>(attributes);
        // Forzamos que 'name' sea siempre el email usado para crear el usuario
        modifiedAttributes.put("name", email);
        modifiedAttributes.put("username", email);

    // 6. Devolver OAuth2User
    // Usamos "name" como nameAttributeKey para que authentication.getName() devuelva el email
    System.out.println("OAuth2User creado con name=" + email + ", authorities=" + authorities);
        return new DefaultOAuth2User(authorities, modifiedAttributes, "name");
    }
}
