package nexora.ejd.security;
import nexora.ejd.services.UserService;
import nexora.ejd.entites.User;
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
    private final UserService userService;

    public CustomOAuth2UserService(UserService userService) {
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

        // If email is null for GitHub, you must request user:email or fetch via separate API
        if (email == null) {
            // As fallback, build a synthetic email (not ideal)
            email = registrationId + "_" + providerId + "@example.com";
        }

        // Persist or update user in DB
        User local = userService.findOrCreateOAuthUser(registrationId, providerId, username, name, email);

        // Create authorities
        List<SimpleGrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));

        // You can add local user id or roles into attributes if useful
        Map<String, Object> mappedAttrs = new HashMap<>(attributes);
        mappedAttrs.put("localUserId", local.getId());
        mappedAttrs.put("localName", local.getName());

        return new DefaultOAuth2User(authorities, mappedAttrs, "id"); // "id" key used as principalNameKey
    }
}
