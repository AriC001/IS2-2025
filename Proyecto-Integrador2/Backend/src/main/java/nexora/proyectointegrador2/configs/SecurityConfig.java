package nexora.proyectointegrador2.configs;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

  private final JwtAuthenticationFilter jwtAuthenticationFilter;

  public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
    this.jwtAuthenticationFilter = jwtAuthenticationFilter;
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        .cors(cors -> cors.configurationSource(corsConfigurationSource()))
        .csrf(csrf -> csrf.disable())
        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authorizeHttpRequests(auth -> auth
          // IMPORTANTE: El orden de las reglas es crítico. Las más específicas primero.
          
          // Permitir OPTIONS requests (preflight CORS) - debe ir primero
          .requestMatchers(org.springframework.http.HttpMethod.OPTIONS, "/**").permitAll()
          
          // Permitir endpoints de autenticación
          .requestMatchers("/api/v1/auth/login").permitAll()
          .requestMatchers("/api/v1/auth/**").permitAll()
          .requestMatchers("/api/v1/weather/**").permitAll()
          // Permitir endpoints públicos necesarios para el registro
          .requestMatchers("/api/v1/nacionalidades/**").permitAll()
          .requestMatchers("/api/v1/localidades/**").permitAll()
          
          // Permitir creación y consulta de usuarios desde el frontend
          .requestMatchers(org.springframework.http.HttpMethod.POST, "/api/v1/usuarios").permitAll()
          .requestMatchers(org.springframework.http.HttpMethod.GET, "/api/v1/usuarios/**").permitAll()
          
          // Permitir recursos estáticos
          .requestMatchers("/favicon.ico", "/public/**", "/error").permitAll()
          
          // Proteger otros endpoints de la API (esto va DESPUÉS de las excepciones públicas)
          .requestMatchers("/api/**").authenticated()
          
          // Permitir cualquier otra petición
          .anyRequest().permitAll()
        )
        .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

    return http.build();
  }

  @Bean
  public CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();
    // Allow common local dev origins used by the frontend (adjust as needed)
    configuration.setAllowedOrigins(Arrays.asList("http://localhost:8081", "http://localhost:8082"));
    configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
    configuration.setAllowedHeaders(Arrays.asList("*"));
    configuration.setAllowCredentials(true);
    
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);
    return source;
  }

}