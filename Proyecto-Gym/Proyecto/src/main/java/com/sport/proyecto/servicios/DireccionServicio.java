package com.sport.proyecto.servicios;

import com.sport.proyecto.entidades.Direccion;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DireccionServicio {

  public List<Direccion> buscarTodos() throws Exception {
    return List.of();
  }


  public Direccion buscarPorId(Long id) throws Exception {
    return null;
  }

  public void guardar(Direccion entity) throws Exception {

  }

  public Direccion actualizar(Direccion entity, Long id) throws Exception {
    return null;
  }

  public void eliminarPorId(Long id) throws Exception {

  }
}
