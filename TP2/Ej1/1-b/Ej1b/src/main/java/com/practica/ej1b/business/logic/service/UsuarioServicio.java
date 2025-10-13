package com.practica.ej1b.business.logic.service;

import com.practica.ej1b.business.domain.entity.Usuario;
import com.practica.ej1b.business.persistence.repository.UsuarioRepositorio;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UsuarioServicio {

  @Autowired
  private UsuarioRepositorio usuarioRepositorio;

  @Autowired
  private PasswordEncoder passwordEncoder;

  // Busqueda

  @Transactional
  public List<Usuario> listarUsuario() throws Exception{
    try {
      return usuarioRepositorio.findAll();
    } catch (Exception e) {
      e.printStackTrace();
      throw new Exception("Error al listar los usuarios");
    }
  }

  @Transactional
  public Collection<Usuario> listarUsuarioActivo() throws Exception{
    try {
      return usuarioRepositorio.findAllByEliminadoFalse();
    } catch (Exception e) {
      e.printStackTrace();
      throw new Exception("Error al listar los usuarios");
    }
  }

  @Transactional
  public Usuario buscarUsuario(String id) throws Exception{
    return usuarioRepositorio.findById(id).orElseThrow(() -> new Exception("No se encontró el usuario solicitado"));
  }

  @Transactional
  public Usuario buscarUsuarioPorNombre(String nombreUsuario) {
    return usuarioRepositorio.findUsuarioByNombreUsuarioAndEliminadoFalse(nombreUsuario); // Si no existe, retorna null
  }

  // Escritura

  @Transactional
  public void crearUsuario(String nombreUsuario, String contrasenia, String nombre, String apellido, String correoElectronico, String telefono) throws Exception {
    try {
      Usuario usuario = new Usuario();
      usuario.setNombreUsuario(nombreUsuario);
      usuario.setContrasenia(passwordEncoder.encode(contrasenia));
      usuario.setNombre(nombre);
      usuario.setApellido(apellido);
      usuario.setCorreoElectronico(correoElectronico);
      usuario.setTelefono(telefono);
      usuario.setEliminado(false);
      usuarioRepositorio.save(usuario);
    } catch (Exception e) {
      e.printStackTrace();
      throw new Exception("Error al crear el usuario");
    }
  }

  @Transactional
  public void modificarUsuario(String id, String nombreUsuario, String contrasenia, String nombre, String apellido, String correoElectronico, String telefono) throws Exception {
    try {
      Usuario usuario = buscarUsuario(id);
      if (nombreUsuario != null && !nombreUsuario.isEmpty()) {
        usuario.setNombreUsuario(nombreUsuario);
      }
      if (contrasenia != null && !contrasenia.isEmpty()) {
        usuario.setContrasenia(passwordEncoder.encode(contrasenia));
      }
      if (nombre != null && !nombre.isEmpty()) {
        usuario.setNombre(nombre);
      }
      if (apellido != null && !apellido.isEmpty()) {
        usuario.setApellido(apellido);
      }
      if (correoElectronico != null && !correoElectronico.isEmpty()) {
        usuario.setCorreoElectronico(correoElectronico);
      }
      if (telefono != null && !telefono.isEmpty()) {
        usuario.setTelefono(telefono);
      }
      usuarioRepositorio.save(usuario);
    } catch (Exception e) {
      e.printStackTrace();
      throw new Exception("Error al modificar el usuario");
    }
  }

  @Transactional
  public void eliminarUsuario(String id) throws Exception {
    try {
      Usuario usuario = buscarUsuario(id);
      usuario.setEliminado(true);
      usuarioRepositorio.save(usuario);
    } catch (Exception e) {
      e.printStackTrace();
      throw new Exception("Error al eliminar el usuario");
    }
  }

  @Transactional
  public void cambiarContrasenia(String id, String contraseniaActual,
                                 String nuevaContrasenia, String nuevaConstraseniaConfirmada) throws Exception {
    try {
      Usuario usuario = buscarUsuario(id);
      if (!passwordEncoder.matches(contraseniaActual, usuario.getContrasenia())) {
        throw new Exception("La contraseña actual es incorrecta");
      }
      if (nuevaContrasenia == null || nuevaContrasenia.isEmpty()) {
        throw new Exception("La nueva contraseña no puede estar vacía");
      }
      if (!nuevaContrasenia.equals(nuevaConstraseniaConfirmada)) {
        throw new Exception("La nueva contraseña y su confirmación no coinciden");
      }
      usuario.setContrasenia(passwordEncoder.encode(nuevaContrasenia));
      usuarioRepositorio.save(usuario);
    } catch (Exception e) {
      e.printStackTrace();
      throw new Exception("Error al cambiar la contraseña: " + e.getMessage());
    }
  }
}
