package com.sport.proyecto.servicios;

import com.sport.proyecto.entidades.Departamento;
import com.sport.proyecto.entidades.Provincia;
import com.sport.proyecto.errores.ErrorServicio;
import com.sport.proyecto.repositorios.ProvinciaRepositorio;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProvinciaServicio implements ServicioBase<Provincia>{
  @Autowired
  private ProvinciaRepositorio repositorio;

  // Busqueda

  @Transactional
  @Override
  public List<Provincia> buscarTodos() throws ErrorServicio {
    List<Provincia> provincias = repositorio.findAll();
    if (provincias.isEmpty()) {
      throw new ErrorServicio("No hay provincias cargadas");
    }
    return repositorio.findAll();
  }

  @Transactional
  public List<Provincia> buscarTodosActivos() throws ErrorServicio {
    List<Provincia> provincias = repositorio.buscarTodosActivos();
    if (provincias.isEmpty()) {
      throw new ErrorServicio("No hay provincias activas");
    }
    return provincias;
  }

  @Transactional
  @Override
  public Provincia buscarPorId(Long id) throws ErrorServicio {
    Optional<Provincia> opt = repositorio.findById(id);
    if (opt.isPresent()) {
      Provincia provincia = opt.get();
      return provincia;
    } else {
      throw new ErrorServicio("No se encontro la provincia solicitada");
    }
  }

  // Escritura

  @Transactional
  @Override
  public void guardar(Provincia provincia) throws ErrorServicio {
    validar(provincia);
    provincia.setEliminado(false);
    repositorio.save(provincia);
  }

  @Transactional
  @Override
  public Provincia actualizar(Provincia provincia, Long id) throws ErrorServicio {
    validar(provincia);
    Optional<Provincia> opt = repositorio.findById(id);
    if (opt.isPresent()) {
      Provincia provinciaActual = opt.get();
      provinciaActual.setNombre(provincia.getNombre());
      return repositorio.save(provinciaActual);
    } else {
      throw new ErrorServicio("No se encontro la provincia solicitada");
    }
  }

  // Eliminacion

  @Transactional
  @Override
  public void eliminarPorId(Long id) throws ErrorServicio {
    Optional<Provincia> opt = repositorio.findById(id);
    if (opt.isPresent()) {
      Provincia provincia = opt.get();
      provincia.setEliminado(true);
      repositorio.save(provincia);
    } else {
      throw new ErrorServicio("No se encontro la provincia solicitada");
    }
  }

  // Validacion

  private void validar(Provincia provincia) throws ErrorServicio {
    if (provincia == null) {
      throw new ErrorServicio("El país no puede ser nulo");
    }
    if (provincia.getNombre() == null || provincia.getNombre().isEmpty()) {
      throw new ErrorServicio("El nombre del país no puede ser nulo o vacío");
    }
    Provincia provinciaExistente = repositorio.buscarPorNombre(provincia.getNombre());
    if (provinciaExistente != null) {
      throw new ErrorServicio("El país ya con nombre " + provincia.getNombre() + " ya existe");
    }
  }
}
