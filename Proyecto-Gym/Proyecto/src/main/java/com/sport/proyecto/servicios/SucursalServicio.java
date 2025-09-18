package com.sport.proyecto.servicios;

import com.sport.proyecto.entidades.Sucursal;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SucursalServicio {

  @Transactional
  public List<Sucursal> buscarTodos() throws Exception {
    return List.of();
  }

  @Transactional
  public Sucursal buscarPorId(Long id) throws Exception {
    return null;
  }

  @Transactional
  public void guardar(Sucursal entity) throws Exception {

  }

  @Transactional
  public Sucursal actualizar(Sucursal entity, Long id) throws Exception {
    return null;
  }

  @Transactional
  public void eliminarPorId(Long id) throws Exception {

  }
}
