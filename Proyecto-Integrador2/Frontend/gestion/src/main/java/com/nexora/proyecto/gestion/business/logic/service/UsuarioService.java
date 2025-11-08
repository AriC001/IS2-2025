package com.nexora.proyecto.gestion.business.logic.service;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import com.nexora.proyecto.gestion.business.persistence.dao.UsuarioDAO;
import com.nexora.proyecto.gestion.dto.UsuarioDTO;


@Service
public class UsuarioService extends BaseService<UsuarioDTO, String> {

  private static final int MIN_USERNAME_LENGTH = 4;
  private static final int MIN_PASSWORD_LENGTH = 6;
  private static final String USERNAME_PATTERN = "^[a-zA-Z0-9._-]{4,}$";

  public UsuarioService(UsuarioDAO dao) {
    super(dao);
  }

  @Override
  protected void validateEntity(UsuarioDTO entity) throws Exception {
    if (entity.getNombreUsuario() == null || entity.getNombreUsuario().trim().isEmpty()) {
      throw new Exception("El nombre de usuario es obligatorio");
    }
    if (entity.getNombreUsuario().length() < MIN_USERNAME_LENGTH) {
      throw new Exception("El nombre de usuario debe tener al menos " + MIN_USERNAME_LENGTH + " caracteres");
    }
    if (!entity.getNombreUsuario().matches(USERNAME_PATTERN)) {
      throw new Exception("El nombre de usuario solo puede contener letras, números, puntos, guiones y guiones bajos");
    }
    if (entity.getClave() == null || entity.getClave().trim().isEmpty()) {
      throw new Exception("La clave es obligatoria");
    }
    if (entity.getClave().length() < MIN_PASSWORD_LENGTH) {
      throw new Exception("La clave debe tener al menos " + MIN_PASSWORD_LENGTH + " caracteres");
    }
    if (entity.getRol() == null) {
      throw new Exception("El rol es obligatorio");
    }
  }

    public Optional<UsuarioDTO> findByUsername(String nombreUsuario) {
      if (nombreUsuario == null || nombreUsuario.trim().isEmpty()) {
        return Optional.empty();
      }
      // Buscar entre las entidades activas expuestas por la API
      List<UsuarioDTO> usuarios = dao.findAllActives();
      return usuarios.stream()
          .filter(u -> nombreUsuario.equals(u.getNombreUsuario()))
          .findFirst();
    }
}

