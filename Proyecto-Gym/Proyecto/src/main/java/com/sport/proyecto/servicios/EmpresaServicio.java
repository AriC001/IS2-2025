package com.sport.proyecto.servicios;

import com.sport.proyecto.entidades.Empresa;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmpresaServicio {

  public List<Empresa> buscarTodos() throws Exception {
    return List.of();
  }

  public Empresa buscarPorId(Long id) throws Exception {
    return null;
  }

  public void guardar(Empresa entity) throws Exception {

  }

  public Empresa actualizar(Empresa entity, Long id) throws Exception {
    return null;
  }

  public void eliminarPorId(Long id) throws Exception {

  }
}
