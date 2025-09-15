package com.sport.proyecto.servicios;

import com.sport.proyecto.entidades.Sucursal;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SucursalServicio implements ServicioBase<Sucursal> {

  @Override
  public List<Sucursal> buscarTodos() throws Exception {
    return List.of();
  }

  @Override
  public Sucursal buscarPorId(Long id) throws Exception {
    return null;
  }

  @Override
  public void guardar(Sucursal entity) throws Exception {

  }

  @Override
  public Sucursal actualizar(Sucursal entity, Long id) throws Exception {
    return null;
  }

  @Override
  public void eliminarPorId(Long id) throws Exception {

  }
}
