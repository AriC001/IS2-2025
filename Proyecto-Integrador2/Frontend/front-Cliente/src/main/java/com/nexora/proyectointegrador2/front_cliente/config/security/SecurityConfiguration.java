package  com.nexora.proyectointegrador2.front_cliente.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;



@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfiguration {

    @Autowired
    private CustomAuthenticationSuccessHandler successHandler;
    // Maneja qué hacer DESPUÉS de un login exitoso (redirigir según el rol)

    @Autowired
    private CustomLogoutSuccessHandler logoutSuccessHandler;
    // Maneja qué hacer DESPUÉS de un logout (redirigir a /index)

    @Autowired
    private BackendAuthenticationProvider backendAuthenticationProvider;
    // Carga usuarios desde tu base de datos para el login con formulario

    @Autowired
    private CustomOAuth2UserService customOAuth2UserService;
    // Maneja el login con GitHub OAuth2 y crea usuarios SOCIO automáticamente
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        // Allow public access to the home page (index), registration endpoints and static resources
                        .requestMatchers("/", "/registro/**", "/persona/registro", "/css/**", "/js/**", "/images/**", "/vendor/**", "/scss/**").permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/auth/login")
                        .usernameParameter("nombreUsuario")   
                        .passwordParameter("clave")           
                        .successHandler(successHandler)
                        .permitAll()
                )
                .oauth2Login(oauth2 -> oauth2
                        .loginPage("/auth/login")
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(customOAuth2UserService)
                        )
                        .successHandler(successHandler)
                )
                // register authentication provider explicitly so the PasswordEncoder bean is used
                .authenticationProvider(authenticationProvider())
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .deleteCookies("JSESSIONID", "SESSION")
                        .invalidateHttpSession(true)
                        .clearAuthentication(true)
                        .logoutSuccessHandler(logoutSuccessHandler)
                );
        http.csrf(csrf -> csrf.disable());
        
        return http.build();
    }

        @Bean
        public org.springframework.security.authentication.AuthenticationProvider authenticationProvider() {
                // Use backend-based authentication provider that delegates to AuthService
                return backendAuthenticationProvider;
        }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


}
