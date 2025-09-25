package com.sport.proyecto.servicios;

import com.sport.proyecto.entidades.Promocion;
import com.sport.proyecto.errores.ErrorServicio;
import com.sport.proyecto.repositorios.PromocionRepositorio;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Date;

@Service
public class PromocionServicio {

  @Autowired
  private PromocionRepositorio promocionRepositorio;

  @Autowired
  private UsuarioServicio usuarioServicio;

  // Busqueda

  @Transactional
  public Collection<Promocion> listarPromocion() throws ErrorServicio {
    try {
      return promocionRepositorio.findAll();
    } catch (Exception e) {
      e.printStackTrace();
      throw new ErrorServicio("Error al listar las promociones");
    }
  }

  @Transactional
  public Collection<Promocion> listarPromocionActiva() throws ErrorServicio {
    try {
      return promocionRepositorio.findAllActives();
    } catch (Exception e) {
      e.printStackTrace();
      throw new ErrorServicio("Error al listar las promociones activas");
    }
  }

  @Transactional
  public Promocion buscarPromocion(String id) throws ErrorServicio {
    try{
      return promocionRepositorio.findById(id).get();
    } catch (Exception e) {
      e.printStackTrace();
      throw new ErrorServicio("Error al buscar la promocion");
    }
  }

  // Alta

  @Transactional
  public void crearPromocion(Date fechaPromocion, String titulo, String texto, String idUsuario) throws ErrorServicio {
    validar(fechaPromocion, titulo, texto, idUsuario);
    try {
      Promocion promocion = new Promocion();
      promocion.setFechaEnvioPromocion(fechaPromocion);
      promocion.setTitulo(titulo);
      promocion.setTexto(texto);
      promocion.setCantidadSociosEnviados(0);
      promocion.setUsuario(usuarioServicio.buscarUsuario(idUsuario));
      promocion.setEliminado(false);
      promocionRepositorio.save(promocion);
    } catch (Exception e) {
      e.printStackTrace();
      throw new ErrorServicio("Error al crear la promocion");
    }
  }

  // Modificacion

  @Transactional
  public void modificarPromocion(String id, Date fechaPromocion, String titulo, String texto, String idUsuario) throws ErrorServicio {
    validar(fechaPromocion, titulo, texto, idUsuario);
    try {
      Promocion promocion = buscarPromocion(id);
      if (promocion == null) {
        throw new ErrorServicio("No se encontro la promocion solicitada");
      }
      promocion.setFechaEnvioPromocion(fechaPromocion);
      promocion.setTitulo(titulo);
      promocion.setTexto(texto);
      promocion.setUsuario(usuarioServicio.buscarUsuario(idUsuario));
      promocionRepositorio.save(promocion);
    } catch (Exception e) {
      e.printStackTrace();
      throw new ErrorServicio("Error al modificar la promocion");
    }
  }

  // Baja

  @Transactional
  public void eliminarPromocion(String id) throws ErrorServicio {
    try {
      Promocion promocion = buscarPromocion(id);
      if (promocion == null) {
        throw new ErrorServicio("No se encontro la promocion solicitada");
      }
      promocion.setEliminado(true);
      promocionRepositorio.save(promocion);
    } catch (Exception e) {
      e.printStackTrace();
      throw new ErrorServicio("Error al eliminar la promocion");
    }
  }

  // Validacion

  private void validar(Date fechaPromocion, String titulo, String texto, String idUsuario) throws ErrorServicio {
    if (fechaPromocion == null) {
      throw new ErrorServicio("La fecha de la promocion no puede ser nula");
    }
    if (titulo == null || titulo.isEmpty()) {
      throw new ErrorServicio("El titulo de la promocion no puede ser nulo o vacio");
    }
    if (texto == null || texto.isEmpty()) {
      throw new ErrorServicio("El texto de la promocion no puede ser nulo o vacio");
    }
    if (idUsuario == null || idUsuario.isEmpty()) {
      throw new ErrorServicio("El id del usuario no puede ser nulo o vacio");
    }
  }


}
