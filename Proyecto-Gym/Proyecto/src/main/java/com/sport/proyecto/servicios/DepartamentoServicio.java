package com.sport.proyecto.servicios;

import com.sport.proyecto.entidades.Departamento;
import com.sport.proyecto.errores.ErrorServicio;
import com.sport.proyecto.repositorios.DepartamentoRepositorio;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DepartamentoServicio {
  @Autowired
  private DepartamentoRepositorio repositorio;

  // Busqueda

  @Transactional
  public List<Departamento> buscarTodos() throws ErrorServicio {
    List<Departamento> departamentos = repositorio.findAll();
    if (departamentos.isEmpty()) {
      throw new ErrorServicio("No hay departamentos cargados");
    }
    return departamentos;
  }

  @Transactional
  public List<Departamento> buscarTodosActivos() throws ErrorServicio {
    List<Departamento> departamentos = repositorio.buscarTodosActivos();
    if (departamentos.isEmpty()) {
      throw new ErrorServicio("No hay departamentos activos");
    }
    return departamentos;
  }

  @Transactional
  public Departamento buscarPorId(Long id) throws ErrorServicio {
    Optional<Departamento> opt = repositorio.findById(id);
    if (opt.isPresent()) {
      Departamento departamento = opt.get();
      return departamento;
    } else {
      throw new ErrorServicio("No se encontro el departamento solicitado");
    }

  }

  // Escritura

  @Transactional
  public void guardar(Departamento departamento) throws ErrorServicio {
    validar(departamento);
    departamento.setEliminado(false);
    repositorio.save(departamento);
  }

  @Transactional
  public Departamento actualizar(Departamento departamento, Long id) throws ErrorServicio {
    validar(departamento);
    Optional<Departamento> opt = repositorio.findById(id);
    if (opt.isPresent()) {
      Departamento departamentoActualizado = opt.get();
      departamentoActualizado.setNombre(departamento.getNombre());
      return repositorio.save(departamentoActualizado);
    } else {
      throw new ErrorServicio("No se encontro el departamento solicitado");
    }
  }

  // Eliminacion

  @Transactional
  public void eliminarPorId(Long id) throws ErrorServicio {
    Optional<Departamento> opt = repositorio.findById(id);
    if (opt.isPresent()) {
      Departamento departamento = opt.get();
      departamento.setEliminado(true);
      repositorio.save(departamento);
    } else {
      throw new ErrorServicio("No se encontro el departamento solicitado");
    }
  }

  // Validacion

  private void validar(Departamento departamento) throws ErrorServicio {
    if (departamento == null) {
      throw new ErrorServicio("El país no puede ser nulo");
    }
    if (departamento.getNombre() == null || departamento.getNombre().isEmpty()) {
      throw new ErrorServicio("El nombre del país no puede ser nulo o vacío");
    }
    Departamento departamentoExistente = repositorio.buscarPorNombre(departamento.getNombre());
    if (departamentoExistente != null) {
      throw new ErrorServicio("El país ya con nombre " + departamento.getNombre() + " ya existe");
    }
  }

}
