package nexora.ejd.entites;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Setter
@Getter
@Entity
@Table(name = "users")
public class User {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String provider;       // e.g. "github"
    private String providerId;     // id from provider
    private String username;
    private String name;
    private String email;

    private String roles; // comma separated, e.g. "ROLE_USER"

    private Instant createdAt;
    private Instant updatedAt;

    // getters/setters, constructors omitted for brevity
}
