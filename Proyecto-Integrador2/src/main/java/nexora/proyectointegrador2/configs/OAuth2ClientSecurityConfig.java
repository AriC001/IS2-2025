package nexora.proyectointegrador2.configs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
@EnableWebSecurity
public class OAuth2ClientSecurityConfig {
        @Autowired
        AuthenticationSuccessHandler successHandler;

        @Bean
        public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
                http
                        //.csrf(csrf -> csrf.disable())
                        .authorizeHttpRequests(req -> {
                                req.requestMatchers("/registration/**", "/login/**", "/css/**", "/js/**", "/images/**").permitAll();
                                req.anyRequest().authenticated();
                        })
                        .formLogin(form -> form
                                        .loginPage("/login")
                                        .successHandler(successHandler))
                        .logout(logout -> logout
                                        .logoutUrl("/logout")
                                        .logoutSuccessUrl("/login"))
                        .oauth2Login(login -> login
                                        .loginPage("/login")
                                        .successHandler(successHandler));
                return http.build();
        }
}