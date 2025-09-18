package com.sport.proyecto.servicios;

import com.sport.proyecto.entidades.Localidad;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LocalidadServicio {

  public List<Localidad> buscarTodos() throws Exception {
    return List.of();
  }

  public Localidad buscarPorId(Long id) throws Exception {
    return null;
  }

  public void guardar(Localidad entity) throws Exception {

  }

  public Localidad actualizar(Localidad entity, Long id) throws Exception {
    return null;
  }

  public void eliminarPorId(Long id) throws Exception {
  }
}
