package com.practica.ej2b.controller.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.practica.ej2b.config.JwtUtil;
import com.practica.ej2b.security.dto.AuthRequest;
import com.practica.ej2b.security.dto.AuthResponse;
import com.practica.ej2b.business.persistence.repository.PersonaRepository;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final PersonaRepository personaRepository;
    private final JwtUtil jwtUtil;

    public AuthController(PersonaRepository personaRepository, JwtUtil jwtUtil) {
        this.personaRepository = personaRepository;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest request) {
        String dni = request.getDni();
        if (dni == null || dni.isBlank()) {
            return ResponseEntity.badRequest().build();
        }

        if (personaRepository.existsByDni(dni)) {
            String token = jwtUtil.generateToken(dni);
            return ResponseEntity.ok(new AuthResponse(token));
        } else {
            return ResponseEntity.status(401).build();
        }
    }
}
