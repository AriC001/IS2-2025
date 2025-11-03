package edu.egg.tinder.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtDecoders;
import org.springframework.security.oauth2.jwt.JwtValidators;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

  @Autowired
  private CustomAuthenticationSuccessHandler successHandler;

  @Autowired
  private CustomLogoutSuccessHandler logoutSuccessHandler;

  @Autowired
  private CustomAuthorizationRequestResolver authorizationRequestResolver;

  @Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri:}")
  private String issuerUri;

  @Value("${auth0.audience:}")
  private String audience;

  // Configuración de la seguridad web con OAuth2 + JWT
  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
      .authorizeHttpRequests(auth -> auth
        .requestMatchers("/scss/**", "/css/**", "/js/**", "/img/**", "/json/**", "/vendor/**", "/static/**").permitAll()
        .requestMatchers("/", "/registro", "/usuario/registrar", "/login**", "/error").permitAll()
        .requestMatchers("/auth0/token/**", "/auth0/debug", "/auth0/get-access-token").permitAll() // Endpoints públicos de Auth0
        .anyRequest().authenticated()
      )
      // Form Login tradicional (email/password de BD)
      .formLogin(form -> form
        .loginPage("/login")
        .loginProcessingUrl("/logincheck")
        .usernameParameter("email")
        .passwordParameter("clave")
        .successHandler(successHandler)
        .failureUrl("/login?error=true")
        .permitAll()
      )
      // OAuth2 Login con Auth0
      // Configuración para redirigir directamente a Auth0 sin página intermedia
      .oauth2Login(oauth2 -> oauth2
        .loginPage("/oauth2/authorization/auth0")
        .authorizationEndpoint(authorization -> authorization
          .authorizationRequestResolver(authorizationRequestResolver)
        )
        .successHandler(successHandler)
      )
      // OAuth2 Resource Server (para validar JWTs en APIs)
      .oauth2ResourceServer(oauth2 -> oauth2
        .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter()))
      )
      .logout(logout -> logout
        .logoutUrl("/logout")
        .logoutSuccessHandler(logoutSuccessHandler)
        .permitAll()
      );

    return http.build();
  }

  // Decodificador de JWT con validación de audience e issuer
  @Bean
  public JwtDecoder jwtDecoder() {
    if (issuerUri == null || issuerUri.isEmpty()) {
      // Fallback si no está configurado (evita errores en desarrollo)
      return null;
    }
    
    NimbusJwtDecoder jwtDecoder = JwtDecoders.fromIssuerLocation(issuerUri);
    
    OAuth2TokenValidator<Jwt> audienceValidator = new AudienceValidator(audience);
    OAuth2TokenValidator<Jwt> withIssuer = JwtValidators.createDefaultWithIssuer(issuerUri);
    OAuth2TokenValidator<Jwt> withAudience = new DelegatingOAuth2TokenValidator<>(withIssuer, audienceValidator);
    
    jwtDecoder.setJwtValidator(withAudience);
    return jwtDecoder;
  }

  // Convierte los claims del JWT en authorities de Spring Security
  @Bean
  public JwtAuthenticationConverter jwtAuthenticationConverter() {
    JwtGrantedAuthoritiesConverter grantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
    // Auth0 usa "permissions" en el JWT para los scopes/permisos
    grantedAuthoritiesConverter.setAuthoritiesClaimName("permissions");
    grantedAuthoritiesConverter.setAuthorityPrefix("ROLE_");

    JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
    jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(grantedAuthoritiesConverter);
    return jwtAuthenticationConverter;
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

}
