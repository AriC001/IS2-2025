package com.sport.proyecto.config.security;

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
import org.springframework.transaction.annotation.Transactional;

import com.sport.proyecto.entidades.Socio;
import com.sport.proyecto.entidades.Usuario;
import com.sport.proyecto.enums.Rol;
import com.sport.proyecto.repositorios.PersonaRepositorio;
import com.sport.proyecto.repositorios.SocioRepositorio;
import com.sport.proyecto.servicios.UsuarioServicio;

/**
 * Servicio personalizado de OAuth2 que crea automáticamente un Usuario con rol SOCIO
 * la primera vez que alguien se loguea con GitHub.
 */
@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final DefaultOAuth2UserService delegate = new DefaultOAuth2UserService();

    private final UsuarioServicio usuarioServicio;
    private final PersonaRepositorio personaRepositorio;
    private final SocioRepositorio socioRepositorio;

    public CustomOAuth2UserService(
            UsuarioServicio usuarioServicio,
            PersonaRepositorio personaRepositorio,
            SocioRepositorio socioRepositorio) {
        this.usuarioServicio = usuarioServicio;
        this.personaRepositorio = personaRepositorio;
        this.socioRepositorio = socioRepositorio;
    }

    @Override
    @Transactional
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

        // 3. Buscar o crear usuario local
        try {
            Usuario usuario = usuarioServicio.buscarUsuarioPorNombre(email);
            
            if (usuario == null) {
                // Usuario no existe - crear Usuario + Socio
                System.out.println("Creando nuevo usuario SOCIO para OAuth2: " + email);
                
                // Crear usuario con contraseña aleatoria (no usable por formulario)
                String randomPwd = UUID.randomUUID().toString().substring(0, 8);
                usuario = usuarioServicio.crearUsuario(email, randomPwd, Rol.SOCIO);

                // Extraer nombre de GitHub
                String nombre = (String) attributes.getOrDefault("name", "");
                if (nombre == null || nombre.isEmpty()) {
                    nombre = email;
                }

                // Crear entidad Socio asociada
                Socio socio = Socio.builder()
                        .nombre(nombre)
                        .apellido("")
                        .email(email)
                        .usuario(usuario)
                        .eliminado(false)
                        .build();

                // Asignar número de socio incremental
                try {
                    Long ultimoNumero = socioRepositorio.obtenerUltimoNumeroSocio();
                    socio.setNumeroSocio(ultimoNumero != null ? ultimoNumero + 1 : 1L);
                } catch (Exception e) {
                    socio.setNumeroSocio(1L);
                }

                personaRepositorio.save(socio);
                System.out.println("Usuario SOCIO creado exitosamente: " + email + " (nro: " + socio.getNumeroSocio() + ")");
            } else {
                System.out.println("Usuario OAuth2 ya existe: " + email);
            }
        } catch (Exception e) {
            System.err.println("Error creando usuario OAuth2: " + e.getMessage());
            e.printStackTrace();
            throw new OAuth2AuthenticationException("Error al crear usuario local: " + e.getMessage());
        }

        // 4. Construir authorities con ROLE_SOCIO
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_SOCIO"));

        // 5. Construir attributes modificados con el email/login como name principal
        Map<String, Object> modifiedAttributes = new HashMap<>(attributes);
        // Forzamos que 'name' sea siempre el email usado para crear el usuario
        modifiedAttributes.put("name", email);
        modifiedAttributes.put("username", email);

        // 6. Devolver OAuth2User con rol SOCIO
        // Usamos "name" como nameAttributeKey para que authentication.getName() devuelva el email
        System.out.println("OAuth2User creado con name=" + email + ", authorities=ROLE_SOCIO");
        return new DefaultOAuth2User(authorities, modifiedAttributes, "name");
    }
}
