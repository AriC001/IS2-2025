package com.is.biblioteca.configuration.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfiguration {

  @Autowired
  private CustomAuthenticationSuccessHandler successHandler;
  @Autowired
  private CustomOAuth2UserService customOAuth2UserService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
      .authorizeHttpRequests(auth -> auth
        .requestMatchers("/admin/**").hasRole("ADMIN")
        .requestMatchers("/prestamo/listar", "/prestamo/registrar", "/prestamo/modificar/**", "/prestamo/eliminar/**").hasRole("ADMIN")
        .requestMatchers("/prestamo/mis-prestamos").hasAnyRole("USER", "ADMIN")
        .requestMatchers("/css/**", "/js/**", "/img/**", "/usuario/registro", "/usuario/registrar").permitAll()
        .anyRequest().authenticated()
      )
      .formLogin(form -> form
        .loginPage("/usuario/login")
        .usernameParameter("email")
        .passwordParameter("password")
        .successHandler(successHandler)
        .permitAll()
      )
      .logout(logout -> logout
        .logoutUrl("/usuario/logout")
        .logoutSuccessUrl("/usuario/login?logout")
        .permitAll())
            .oauth2Login(login -> login
                    .loginPage("/login")
                    .userInfoEndpoint(u -> u.userService(customOAuth2UserService))
                    .successHandler(successHandler)
                    .permitAll());
    return http.build();
  }

    @Bean
    public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public AuthenticationSuccessHandler authenticationSuccessHandler() {
    SimpleUrlAuthenticationSuccessHandler handler = new SimpleUrlAuthenticationSuccessHandler("/");
    handler.setAlwaysUseDefaultTargetUrl(true);
    return handler;
  }

}
