package nexora.ejd.security;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

@Configuration
@EnableWebSecurity
public class OAuth2ClientSecurityConfig {

    // Inject success handler as a parameter to the filterChain method to avoid
    // a circular bean reference between SecurityFilterChain and the handler.

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, AuthenticationSuccessHandler successHandler) throws Exception {
        http
                //.csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(req -> {
                    // Permit the login page, OAuth2 endpoints and static resources
                    req.requestMatchers(
                            "/registration/**",
                            "/login",
                            "/login/**",
                            "/oauth2/**",
                            "/login/oauth2/**",
                            "/css/**",
                            "/js/**",
                            "/images/**",
                            "/favicon.ico",
                            "/error"
                    ).permitAll();
                    req.anyRequest().authenticated();
                })
        .formLogin(form -> form
            .loginPage("/login")
            // your login form uses 'nombreUsuario' and 'clave' as input names
            .usernameParameter("nombreUsuario")
            .passwordParameter("clave")
            .successHandler(successHandler)
            .permitAll())
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login"))
        .oauth2Login(login -> login
            .loginPage("/login")
        .successHandler(successHandler)
        .permitAll());
        return http.build();
    }

    /*@Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }*/

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Provide a default AuthenticationSuccessHandler so `successHandler` is never null.
     * Redirects to the application root after successful authentication.
     */
    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler() {
        SimpleUrlAuthenticationSuccessHandler handler = new SimpleUrlAuthenticationSuccessHandler("/");
        handler.setAlwaysUseDefaultTargetUrl(true);
        return handler;
    }
}
