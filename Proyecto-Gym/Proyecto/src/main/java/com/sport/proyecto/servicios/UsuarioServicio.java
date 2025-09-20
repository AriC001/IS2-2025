package com.sport.proyecto.servicios;

import com.sport.proyecto.entidades.Persona;
import com.sport.proyecto.entidades.Usuario;
import com.sport.proyecto.enums.Rol;
import com.sport.proyecto.errores.ErrorServicio;
import com.sport.proyecto.repositorios.UsuarioRepositorio;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class UsuarioServicio {
  @Autowired
  private UsuarioRepositorio usuarioRepositorio;

  // Busqueda

  @Transactional
  public List<Usuario> listarUsuario() throws ErrorServicio {
    try {
      List<Usuario> usuarios = usuarioRepositorio.findAll();
      if (usuarios.isEmpty()) {
        throw new ErrorServicio("No existen usuarios registrados");
      }
      return usuarios;
    }catch (Exception e) {
      throw new ErrorServicio("Error del sistema");
    }
  }

  @Transactional
  public List<Usuario> listarUsuarioActivo() throws ErrorServicio {
    try {
      List<Usuario> usuarios = usuarioRepositorio.findAllActives();
      if (usuarios.isEmpty()) {
        throw new ErrorServicio("No existen usuarios registrados");
      }
      return usuarios;
    }catch (Exception e) {
      throw new ErrorServicio("Error del sistema");
    }
  }

  @Transactional
  public Usuario buscarUsuario(Long id) throws ErrorServicio {
    try {
      Optional<Usuario> opt = usuarioRepositorio.findById(id);
      if (opt.isPresent()) {
        return opt.get();
      } else {
        throw new ErrorServicio("No se encontro el usuario solicitado");
      }
    }catch (Exception e) {
      throw new ErrorServicio("Error del sistema");
    }
  }

  @Transactional
  public Usuario buscarUsuarioPorNombre(String nombreUsuario) throws ErrorServicio {
    try {
      Usuario usuario = usuarioRepositorio.findByUsername(nombreUsuario);
      if (usuario != null) {
        return usuario;
      } else {
        throw new ErrorServicio("No se encontro el usuario solicitado");
      }
    }catch (Exception e) {
      throw new ErrorServicio("Error del sistema");
    }
  }

  // Escritura

  @Transactional
  public void crearUsuario(String nombreUsuario, String clave, Rol rol) throws ErrorServicio {
    validar(nombreUsuario, clave, rol);
    try{
      if (buscarUsuarioPorNombre(nombreUsuario) != null) {
        throw new ErrorServicio("El nombre de usuario ya existe");
      }
      Usuario usuario = new Usuario();
      usuario.setNombreUsuario(nombreUsuario);
      usuario.setClave(clave);
      usuario.setRol(rol);
      usuario.setEliminado(false);
      usuarioRepositorio.save(usuario);
    }catch (Exception e) {
      throw new ErrorServicio("Error del sistema");
    }
  }

  @Transactional
  public void modificarUsuario(Long id, String nombreUsuario, String clave, Rol rol) throws ErrorServicio {
    validar(nombreUsuario, clave, rol);
    try{
      if (id == null) {
        throw new ErrorServicio("El id no puede ser nulo");
      }
      Usuario usuario = buscarUsuario(id);
      if (usuario == null) {
        throw new ErrorServicio("No se encontro el usuario solicitado");
      }
      usuario.setNombreUsuario(nombreUsuario);
      usuario.setClave(clave);
      usuario.setRol(rol);
      usuarioRepositorio.save(usuario);

    }catch (Exception e) {
      throw new ErrorServicio("Error del sistema");
    }
  }

  // Eliminacion

  @Transactional
  public void eliminarUsuario(Long id) throws ErrorServicio {
    try{
      if (id == null) {
        throw new ErrorServicio("El id no puede ser nulo");
      }
      Usuario usuario = buscarUsuario(id);
      if (usuario == null) {
        throw new ErrorServicio("No se encontro el usuario solicitado");
      }
      usuario.setEliminado(true);
      usuarioRepositorio.save(usuario);
    }catch (Exception e) {
      throw new ErrorServicio("Error del sistema");
    }
  }

  // Login

  @Transactional
  public Usuario login(String nombreUsuario, String clave) throws ErrorServicio {
    try{
      if (nombreUsuario == null || nombreUsuario.isEmpty()) {
        throw new ErrorServicio("El nombre de usuario no puede ser nulo o estar vacio");
      }
      if (clave == null || clave.isEmpty()) {
        throw new ErrorServicio("La clave no puede ser nula o estar vacia");
      }

      Usuario usuario = buscarUsuarioPorNombre(nombreUsuario);
      if (usuario == null) {
        throw new ErrorServicio("No se encontro el usuario solicitado");
      }
      if (!usuario.getClave().equals(clave)) {
        throw new ErrorServicio("La clave es incorrecta");
      }
      return usuario;
    }catch (Exception e) {
      throw new ErrorServicio("Error del sistema");
    }
  }

  // Validacion

  private void validar(String nombreUsuario, String clave, Rol rol) throws ErrorServicio {
    if(nombreUsuario == null || nombreUsuario.isEmpty()){
      throw new ErrorServicio("El nombre de usuario no puede ser nulo o estar vacío");
    }
    if(clave == null || clave.isEmpty()){
      throw new ErrorServicio("La clave no puede ser nula o estar vacía");
    }
    if (clave.length() < 6 || clave.length() > 12) {
      throw new ErrorServicio("La clave debe tener entre 6 y 12 caracteres");
    }
    if(rol == null){
      throw new ErrorServicio("El rol no puede ser nulo");
    }
  }

  public boolean esAdmin(Long id){
    Optional<Usuario> opt = usuarioRepositorio.findById(id);
    if(opt.isPresent()){
      Usuario u = opt.get();
      if(u.getId() == 1L){
        return true;
      }else {
        return false;
      }
    }
    return false;
  }
}
