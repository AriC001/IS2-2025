package com.sport.proyecto.servicios;

import com.sport.proyecto.entidades.Persona;
import com.sport.proyecto.entidades.Usuario;
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

  @Transactional
  public List<Usuario> buscarTodos() throws ErrorServicio {
    if (usuarioRepositorio.findAll().isEmpty()) {
      throw new Error("No hay usuarios cargados");
    }
    return usuarioRepositorio.findAll();
  }

  @Transactional
  public Usuario buscarPorId(Long id) throws ErrorServicio {
    Optional<Usuario> opt = usuarioRepositorio.findById(id);
    if (opt.isPresent()) {
      Usuario usuario = opt.get();
      return usuario;
    } else {
      throw new ErrorServicio("No se encontro el usuario solicitado");
    }
  }

  @Transactional
  public Usuario buscarPorNombreUsuarioYClave(String nombreUsuario, String clave) throws ErrorServicio {
    validar(nombreUsuario, clave);
    Optional<Usuario> opt = usuarioRepositorio.buscarPorNombreUsuarioYClave(nombreUsuario, clave);
    if (opt.isPresent()) {
      return opt.get();
    } else {
      throw new ErrorServicio("No se encontro el usuario solicitado");
    }
  }

  @Transactional
  public void guardar(String nombreUsuario, String clave) throws ErrorServicio {
    validar(nombreUsuario, clave);
    Usuario usuario = new Usuario();
    usuario.setNombreUsuario(nombreUsuario);
    usuario.setClave(clave);
    usuario.setEliminado(false);
    usuarioRepositorio.save(usuario);
  }

  @Transactional
  public Usuario actualizar(String nombreUsuario, String clave, Long id) throws ErrorServicio {
    validar(nombreUsuario, clave);
    Optional<Usuario> opt = usuarioRepositorio.findById(id);
    if (opt.isPresent()) {
      Usuario usuarioActualizado = opt.get();
      usuarioActualizado.setNombreUsuario(nombreUsuario);
      usuarioActualizado.setClave(clave);
      return usuarioRepositorio.save(usuarioActualizado);
    } else {
      throw new ErrorServicio("No se encontro el usuario solicitado");
    }
  }

  @Transactional
  public void eliminarPorId(Long id) throws ErrorServicio {
    Optional<Usuario> opt = usuarioRepositorio.findById(id);
    if (opt.isPresent()) {
      Usuario usuario = opt.get();
      usuario.setEliminado(true);
      usuarioRepositorio.save(usuario);
    } else {
      throw new ErrorServicio("No se encontro el usuario solicitado");
    }
  }

  private void validar(String nombreUsuario, String clave) throws ErrorServicio {
    if(nombreUsuario == null || nombreUsuario.isEmpty()){
      throw new ErrorServicio("El nombre de usuario no puede ser nulo o estar vacío");
    }
    if(clave == null || clave.isEmpty()){
      throw new ErrorServicio("La clave no puede ser nula o estar vacía");
    }
    if (clave.length() < 6 || clave.length() > 12) {
      throw new ErrorServicio("La clave debe tener entre 6 y 12 caracteres");
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
