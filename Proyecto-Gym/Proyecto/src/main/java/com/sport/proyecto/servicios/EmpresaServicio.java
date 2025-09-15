package com.sport.proyecto.servicios;

import com.sport.proyecto.entidades.Empresa;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmpresaServicio implements ServicioBase<Empresa> {
  @Override
  public List<Empresa> buscarTodos() throws Exception {
    return List.of();
  }

  @Override
  public Empresa buscarPorId(Long id) throws Exception {
    return null;
  }

  @Override
  public void guardar(Empresa entity) throws Exception {

  }

  @Override
  public Empresa actualizar(Empresa entity, Long id) throws Exception {
    return null;
  }

  @Override
  public void eliminarPorId(Long id) throws Exception {

  }
}
