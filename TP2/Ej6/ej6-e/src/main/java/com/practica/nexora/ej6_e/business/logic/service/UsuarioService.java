package com.practica.nexora.ej6_e.business.logic.service;

import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.practica.nexora.ej6_e.business.domain.entity.Persona;
import com.practica.nexora.ej6_e.business.domain.entity.Usuario;
import com.practica.nexora.ej6_e.business.persistence.repository.UsuarioRepository;

@Service
public class UsuarioService extends BaseService<Usuario, Long> {

  private final UsuarioRepository usuarioRepository;
  private final PasswordEncoder passwordEncoder;
  private final PersonaService personaService;

  public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder, PersonaService personaService) {
    super(usuarioRepository);
    this.usuarioRepository = usuarioRepository;
    this.passwordEncoder = passwordEncoder;
    this.personaService = personaService;
  }

  public Optional<Usuario> findByNombreUsuario(String nombreUsuario) {
    return usuarioRepository.findByNombreUsuarioAndEliminadoFalse(nombreUsuario);
  }

  @Transactional
  public Usuario registrarUsuario(Usuario usuario, String confirmarClave) {
    // Validar la entidad usuario
    validate(usuario);

    // Validación: confirmación de contraseña
    if (confirmarClave == null || !usuario.getClave().equals(confirmarClave)) {
      throw new IllegalArgumentException("Las contraseñas no coinciden");
    }

    // Validación: verificar que el usuario no exista
    if (existeUsuario(usuario.getNombreUsuario())) {
      throw new IllegalArgumentException("El nombre de usuario ya está en uso");
    }

    // Encriptar la contraseña
    usuario.setClave(passwordEncoder.encode(usuario.getClave()));
    
    // Guardar el usuario primero (sin persona asociada aún)
    Usuario usuarioGuardado = usuarioRepository.save(usuario);
    
    return usuarioGuardado;
  }
  
  @Transactional
  public Persona registrarUsuarioConPersona(Persona persona, Usuario usuario, String confirmarClave) {
    // Primero registrar el usuario
    Usuario usuarioGuardado = registrarUsuario(usuario, confirmarClave);
    
    // Asociar el usuario a la persona
    persona.setUsuario(usuarioGuardado);
    
    // Guardar la persona (cascade guardará el usuario también)
    return personaService.save(persona);
  }

  public boolean existeUsuario(String nombreUsuario) {
    if (nombreUsuario == null || nombreUsuario.trim().isEmpty()) {
      return false;
    }
    return usuarioRepository.findByNombreUsuarioAndEliminadoFalse(nombreUsuario.trim()).isPresent();
  }

  @Override
  protected void validate(Usuario entity) throws IllegalArgumentException {
    if (entity == null) {
      throw new IllegalArgumentException("El usuario no puede ser nulo");
    }

    if (entity.getNombreUsuario() == null || entity.getNombreUsuario().trim().isEmpty()) {
      throw new IllegalArgumentException("El nombre de usuario no puede estar vacío");
    }

    if (entity.getNombreUsuario().trim().length() < 3) {
      throw new IllegalArgumentException("El nombre de usuario debe tener al menos 3 caracteres");
    }

    if (entity.getClave() == null || entity.getClave().trim().isEmpty()) {
      throw new IllegalArgumentException("La contraseña no puede estar vacía");
    }

    // Validación: longitud mínima de la contraseña
    if (entity.getClave().length() < 6) {
      throw new IllegalArgumentException("La contraseña debe tener al menos 6 caracteres");
    }

    // Validar que el nombre de usuario no esté en uso (solo para nuevos usuarios)
    if (entity.getId() == null && existeUsuario(entity.getNombreUsuario())) {
      throw new IllegalArgumentException("El nombre de usuario ya está en uso");
    }
  }

}
