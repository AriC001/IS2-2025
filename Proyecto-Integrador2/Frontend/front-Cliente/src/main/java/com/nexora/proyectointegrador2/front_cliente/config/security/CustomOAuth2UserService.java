package  com.nexora.proyectointegrador2.front_cliente.config.security;

import java.util.*;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.nexora.proyectointegrador2.front_cliente.business.logic.service.UsuarioService;
import com.nexora.proyectointegrador2.front_cliente.dto.UsuarioDTO;
import com.nexora.proyectointegrador2.front_cliente.dto.enums.RolUsuario;

/**
 * Servicio personalizado de OAuth2 que crea automáticamente un Usuario con rol SOCIO
 * la primera vez que alguien se loguea con GitHub.
 */
@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final DefaultOAuth2UserService delegate = new DefaultOAuth2UserService();

    private final UsuarioService usuarioService;
    private final RestTemplate restTemplate;

    @Value("${api.base.url}")
    private String baseUrl;

    public CustomOAuth2UserService(UsuarioService usuarioService, RestTemplate restTemplate) {
        this.usuarioService = usuarioService;
        this.restTemplate = restTemplate;
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

        // Guardar en variable final para usar dentro de lambdas
        final String loginEmail = email;

        // 3. Buscar o crear usuario local usando el access token del proveedor OAuth2
        UsuarioDTO usuario = null;
        String accessToken = null;
        try {
            if (userRequest != null && userRequest.getAccessToken() != null) {
                accessToken = userRequest.getAccessToken().getTokenValue();
            }

            // Si tenemos token del proveedor, usarlo en las llamadas al backend
            if (accessToken != null && !accessToken.isBlank()) {
                HttpHeaders headers = new HttpHeaders();
                headers.setBearerAuth(accessToken);

                // Obtener todos los usuarios activos y buscar por nombreUsuario
                ResponseEntity<UsuarioDTO[]> resp = restTemplate.exchange(
                        baseUrl + "/usuarios",
                        HttpMethod.GET,
                        new HttpEntity<>(headers),
                        UsuarioDTO[].class
                );

                List<UsuarioDTO> usuarios = resp.getBody() != null ? Arrays.asList(resp.getBody()) : List.of();
        Optional<UsuarioDTO> existing = usuarios.stream()
            .filter(u -> loginEmail.equals(u.getNombreUsuario()))
                        .findFirst();

                if (existing.isPresent()) {
            usuario = existing.get();
            System.out.println("Usuario OAuth2 ya existe: " + loginEmail);
                } else {
                    // Crear nuevo usuario directamente pasando el mismo header
            System.out.println("Creando nuevo usuario para OAuth2: " + loginEmail);
                    String randomPwd = UUID.randomUUID().toString().substring(0, 8);

                    UsuarioDTO nuevo = UsuarioDTO.builder()
                .nombreUsuario(loginEmail)
                            .clave(randomPwd)
                            .rol(RolUsuario.CLIENTE)
                            .build();

                    ResponseEntity<UsuarioDTO> created = restTemplate.exchange(
                            baseUrl + "/usuarios",
                            HttpMethod.POST,
                            new HttpEntity<>(nuevo, headers),
                            UsuarioDTO.class
                    );
                    usuario = created.getBody();
                    System.out.println("Usuario creado vía API: " + loginEmail + " (id: " + (usuario != null ? usuario.getId() : "N/A") + ")");
                }
            } else {
                // Fallback: intentar usar el servicio existente (puede requerir token de servicio en config)
                Optional<UsuarioDTO> existing = usuarioService.findByUsername(loginEmail);
                if (existing.isPresent()) {
                    usuario = existing.get();
                    System.out.println("Usuario OAuth2 ya existe (fallback): " + loginEmail);
                } else {
                    System.out.println("Creando nuevo usuario para OAuth2 (fallback): " + loginEmail);
                    String randomPwd = UUID.randomUUID().toString().substring(0, 8);

                    UsuarioDTO nuevo = UsuarioDTO.builder()
                            .nombreUsuario(loginEmail)
                            .clave(randomPwd)
                            .rol(RolUsuario.CLIENTE)
                            .build();

                    usuario = usuarioService.create(nuevo);
                    System.out.println("Usuario creado vía API (fallback): " + loginEmail + " (id: " + (usuario != null ? usuario.getId() : "N/A") + ")");
                }
            }
        } catch (HttpClientErrorException e) {
            System.err.println("Error creando/obteniendo usuario OAuth2 vía API: " + e.getMessage());
            throw new OAuth2AuthenticationException("Error al crear/obtener usuario local: " + e.getMessage());
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
