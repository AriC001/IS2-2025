package com.sport.proyecto.servicios;

import com.sport.proyecto.entidades.Usuario;
import com.sport.proyecto.errores.ErrorServicio;
import com.sport.proyecto.repositorios.UsuarioRepositorio;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class UsuarioServicio implements ServicioBase<Usuario>{
  @Autowired
  private UsuarioRepositorio repositorio;

  @Transactional
  @Override
  public List<Usuario> buscarTodos() throws ErrorServicio {
    if (repositorio.findAll().isEmpty()) {
      throw new Error("No hay usuarios cargados");
    }
    return repositorio.findAll();
  }

  @Transactional
  @Override
  public Usuario buscarPorId(Long id) throws ErrorServicio {
    Optional<Usuario> opt = repositorio.findById(id);
    if (opt.isPresent()) {
      Usuario usuario = opt.get();
      return usuario;
    } else {
      throw new ErrorServicio("No se encontro el usuario solicitado");
    }
  }

  @Transactional
  public Usuario buscarPorNombreUsuarioYClave(Usuario usuario) throws ErrorServicio {
    validar(usuario);
    Optional<Usuario> opt = repositorio.buscarPorNombreUsuarioYClave(usuario.getNombreUsuario(), usuario.getClave());
    if (opt.isPresent()) {
      return opt.get();
    } else {
      throw new ErrorServicio("No se encontro el usuario solicitado");
    }
  }


  @Override
  public void guardar(Usuario usuario) throws ErrorServicio {
    validar(usuario);
    usuario.setEliminado(false);
    repositorio.save(usuario);
  }

  @Override
  public Usuario actualizar(Usuario usuario, Long id) throws ErrorServicio {
    validar(usuario);
    Optional<Usuario> opt = repositorio.findById(id);
    if (opt.isPresent()) {
      Usuario usuarioActualizado = opt.get();
      usuarioActualizado.setNombreUsuario(usuario.getNombreUsuario());
      usuarioActualizado.setClave(usuario.getClave());
      return repositorio.save(usuarioActualizado);
    } else {
      throw new ErrorServicio("No se encontro el usuario solicitado");
    }
  }

  @Override
  public void eliminarPorId(Long id) throws ErrorServicio {
    Optional<Usuario> opt = repositorio.findById(id);
    if (opt.isPresent()) {
      Usuario usuario = opt.get();
      usuario.setEliminado(true);
      repositorio.save(usuario);
    } else {
      throw new ErrorServicio("No se encontro el usuario solicitado");
    }
  }

  private void validar(Usuario usuario) throws ErrorServicio {
    if(usuario.getNombreUsuario() == null || usuario.getNombreUsuario().isEmpty()){
      throw new ErrorServicio("El nombre de usuario no puede ser nulo o estar vacío");
    }
    if(usuario.getClave() == null || usuario.getClave().isEmpty()){
      throw new ErrorServicio("La clave no puede ser nula o estar vacía");
    }
    if (usuario.getClave().length() < 6 || usuario.getClave().length() > 12) {
      throw new ErrorServicio("La clave debe tener entre 6 y 12 caracteres");
    }
  }
}
