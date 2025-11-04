package com.practica.ej2b.config;

import com.practica.ej2b.business.domain.entity.Persona;
import com.practica.ej2b.business.persistence.repository.PersonaRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;
import java.util.Collections;
import java.util.Optional;


@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private PersonaRepository userRepository;
    
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Persona user = new Persona();
        Optional<Persona> opt = userRepository.findByDni(username);
        if(opt.isEmpty()) {
            throw new UsernameNotFoundException("User Not Found with username: " + username);
        }
        else{
            user = opt.get();
        }
        return new User(user.getDni(), "", Collections.emptyList());
    }

}