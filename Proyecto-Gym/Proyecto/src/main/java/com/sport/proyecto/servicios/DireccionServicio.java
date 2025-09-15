package com.sport.proyecto.servicios;

import com.sport.proyecto.entidades.Direccion;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DireccionServicio implements ServicioBase<Direccion> {
  @Override
  public List<Direccion> buscarTodos() throws Exception {
    return List.of();
  }

  @Override
  public Direccion buscarPorId(Long id) throws Exception {
    return null;
  }

  @Override
  public void guardar(Direccion entity) throws Exception {

  }

  @Override
  public Direccion actualizar(Direccion entity, Long id) throws Exception {
    return null;
  }

  @Override
  public void eliminarPorId(Long id) throws Exception {

  }
}
