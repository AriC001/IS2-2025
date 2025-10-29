package com.sport.proyecto.config.security;

import com.sport.proyecto.servicios.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfiguration {

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/login/**","/usuarios/register/**","/scss/**" ,"/registro/**", "/css/**", "/vendor/**").permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .usernameParameter("email")   // <--- importante si querés usar "email"
                        .passwordParameter("password")
                        .defaultSuccessUrl("/index", true)
                        .permitAll()
                )
                // register authentication provider explicitly so the PasswordEncoder bean is used
                .authenticationProvider(authenticationProvider())
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout")
                );
        http.csrf(csrf -> csrf.disable());
        return http.build();
    }

    @Bean
    public org.springframework.security.authentication.dao.DaoAuthenticationProvider authenticationProvider() {
        org.springframework.security.authentication.dao.DaoAuthenticationProvider provider = new org.springframework.security.authentication.dao.DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
                // The application currently stores passwords using UtilServicio.encriptarClave (MD5 hex).
                // To make Spring Security authenticate existing users without re-hashing all passwords,
                // provide a PasswordEncoder that computes MD5 hex and compares equality.
                return new PasswordEncoder() {
                        @Override
                        public String encode(CharSequence rawPassword) {
                                try {
                                        java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
                                        byte[] bytes = md.digest(rawPassword.toString().getBytes());
                                        StringBuilder sb = new StringBuilder(2 * bytes.length);
                                        char[] HEXADECIMAL = {'0','1','2','3','4','5','6','7','8','9','a','b','c','d','e','f'};
                                        for (int i = 0; i < bytes.length; i++) {
                                                int low = (int) (bytes[i] & 0x0f);
                                                int high = (int) ((bytes[i] & 0xf0) >> 4);
                                                sb.append(HEXADECIMAL[high]);
                                                sb.append(HEXADECIMAL[low]);
                                        }
                                        return sb.toString();
                                } catch (Exception e) {
                                        throw new RuntimeException(e);
                                }
                        }

                        @Override
                        public boolean matches(CharSequence rawPassword, String encodedPassword) {
                                if (rawPassword == null && encodedPassword == null) return true;
                                if (rawPassword == null || encodedPassword == null) return false;
                                String hashed = encode(rawPassword);
                                return hashed.equals(encodedPassword);
                        }
                };
  }

}
