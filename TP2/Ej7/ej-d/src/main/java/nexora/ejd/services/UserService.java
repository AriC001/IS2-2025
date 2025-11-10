package nexora.ejd.services;

import nexora.ejd.entites.User;
import nexora.ejd.repositories.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository repo;
    public UserService(UserRepository repo){ this.repo = repo; }

    @Transactional
    public User findOrCreateOAuthUser(String provider, String providerId, String username, String name, String email) {
        Optional<User> found = repo.findByProviderAndProviderId(provider, providerId);
        if (found.isPresent()) {
            User u = found.get();
            boolean changed = false;
            if (name != null && !name.equals(u.getName())) { u.setName(name); changed = true; }
            if (email != null && !email.equals(u.getEmail())) { u.setEmail(email); changed = true; }
            if (changed) { u.setUpdatedAt(Instant.now()); repo.save(u); }
            return u;
        }
        // create new user
        User u = new User();
        u.setProvider(provider);
        u.setProviderId(providerId);
        u.setUsername(username != null ? username : provider + "_" + providerId);
        u.setName(name);
        u.setEmail(email);
        u.setRoles("ROLE_USER");
        u.setCreatedAt(Instant.now());
        u.setUpdatedAt(Instant.now());
        return repo.save(u);
    }
}
