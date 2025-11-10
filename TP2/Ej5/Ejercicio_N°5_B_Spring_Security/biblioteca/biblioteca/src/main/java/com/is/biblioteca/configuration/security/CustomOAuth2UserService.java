package com.is.biblioteca.configuration.security;

import com.is.biblioteca.business.domain.entity.Usuario;
import com.is.biblioteca.business.logic.service.UsuarioService;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final DefaultOAuth2UserService delegate = new DefaultOAuth2UserService();
    private final UsuarioService userService;

    public CustomOAuth2UserService(UsuarioService userService) {
        this.userService = userService;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) {
        OAuth2User oauthUser = delegate.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId(); // "github"
        Map<String, Object> attributes = oauthUser.getAttributes();

        // Extract common info (provider-specific)
        String providerId = String.valueOf(attributes.get("id")); // GitHub: "id" (number)
        String username = (String) attributes.get("login");     // GitHub: "login"
        String name = (String) attributes.get("name");
        String email = (String) attributes.get("email");
        System.out.println(email);

        // If email is null for GitHub, you must request user:email or fetch via separate API
        if (email == null) {
            // As fallback, build a synthetic email (not ideal)
            email = registrationId + "_" + providerId + "@example.com";
        }

        // Persist or update user in DB
        Usuario local = userService.findOrCreateOAuthUser(registrationId, providerId, username, name, email);

        // Create authorities
        List<SimpleGrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));

        // You can add local user id or roles into attributes if useful
        Map<String, Object> mappedAttrs = new HashMap<>(attributes);
        // Ensure the email attribute reflects any fallback/synthetic value we computed above
        mappedAttrs.put("email", email);
        mappedAttrs.put("localUserId", local.getId());
        mappedAttrs.put("localName", local.getNombre());

        return new DefaultOAuth2User(authorities, mappedAttrs, "email"); // "email" key used as principalNameKey
    }
}
