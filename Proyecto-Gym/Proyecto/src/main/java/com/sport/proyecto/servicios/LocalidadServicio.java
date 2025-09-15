package com.sport.proyecto.servicios;

import com.sport.proyecto.entidades.Localidad;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LocalidadServicio implements ServicioBase<Localidad>{
  @Override
  public List<Localidad> buscarTodos() throws Exception {
    return List.of();
  }

  @Override
  public Localidad buscarPorId(Long id) throws Exception {
    return null;
  }

  @Override
  public void guardar(Localidad entity) throws Exception {

  }

  @Override
  public Localidad actualizar(Localidad entity, Long id) throws Exception {
    return null;
  }

  @Override
  public void eliminarPorId(Long id) throws Exception {
  }
}
