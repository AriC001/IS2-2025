package com.sport.proyecto.servicios;

import com.sport.proyecto.entidades.Pais;
import com.sport.proyecto.errores.ErrorServicio;
import com.sport.proyecto.repositorios.PaisRepositorio;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PaisServicio implements ServicioBase<Pais> {
  @Autowired
  private PaisRepositorio repositorio;

  // Busqueda

  @Transactional
  @Override
  public List<Pais> buscarTodos() throws ErrorServicio {
    if (repositorio.findAll().isEmpty()) {
      throw new Error("No hay paises cargados");
    }
    return repositorio.findAll();
  }

  public List<Pais> buscarTodosActivos() throws ErrorServicio {
    List<Pais> paises = repositorio.buscarTodosActivos();
    if (paises.isEmpty()) {
      throw new Error("No hay paises activos");
    }
    return paises;
  }


  @Override
  public Pais buscarPorId(Long id) throws ErrorServicio {
    Optional<Pais> opt = repositorio.findById(id);
    if (opt.isPresent()) {
      Pais pais = opt.get();
      return pais;
    } else {
      throw new ErrorServicio("No se encontro el país solicitado");
    }

  }

  // Escritura

  @Override
  public void guardar(Pais pais) throws ErrorServicio {
    validar(pais);
    boolean falso = false;
    pais.setEliminado(falso);
    repositorio.save(pais);
  }

  @Override
  public Pais actualizar(Pais pais, Long id) throws ErrorServicio {
    validar(pais);
    Optional<Pais> opt = repositorio.findById(id);
    if (opt.isPresent()) {
      Pais paisActualizado = opt.get();
      paisActualizado.setNombre(pais.getNombre());
      return repositorio.save(paisActualizado);
    } else {
      throw new ErrorServicio("No se encontro el país solicitado");
    }
  }

  // Eliminacion

  @Override
  public void eliminarPorId(Long id) throws ErrorServicio {
    Optional<Pais> opt = repositorio.findById(id);
    if (opt.isPresent()) {
      Pais pais = opt.get();
      pais.setEliminado(true);
      repositorio.save(pais);
    } else {
      throw new ErrorServicio("No se encontro el país solicitado");
    }
  }

  // Validacion

  private void validar(Pais pais) throws ErrorServicio {
    if (pais == null) {
      throw new ErrorServicio("El país no puede ser nulo");
    }
    if (pais.getNombre() == null || pais.getNombre().isEmpty()) {
      throw new ErrorServicio("El nombre del país no puede ser nulo o vacío");
    }
    Pais paisExistente = repositorio.buscarPorNombre(pais.getNombre());
    if (paisExistente != null) {
      throw new ErrorServicio("El país ya con nombre " + pais.getNombre() + " ya existe");
    }
  }

}
