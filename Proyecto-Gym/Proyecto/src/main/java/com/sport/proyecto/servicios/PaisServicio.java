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
public class PaisServicio {
  @Autowired
  private PaisRepositorio repositorioPais;

  // Busqueda

  @Transactional
  public List<Pais> listarPais() throws ErrorServicio {
    try {
      List<Pais> paises = repositorioPais.findAll();
      if (paises.isEmpty()) {
        throw new ErrorServicio("No hay paises cargados");
      }
      return paises;
    } catch (Exception e) {
      throw new ErrorServicio("Error del sistema");
    }
  }

  @Transactional
  public List<Pais> listarPaisActivo() throws ErrorServicio {
    try {
      List<Pais> paises = repositorioPais.findAllActives();
      if (paises.isEmpty()) {
        throw new ErrorServicio("No hay paises cargados");
      }
      return paises;
    } catch (Exception e) {
      throw new ErrorServicio("Error del sistema");
    }
  }

  @Transactional
  public Pais buscarPais(Long id) throws ErrorServicio {
    try {
      Optional<Pais> opt = repositorioPais.findById(id);
      if (opt.isPresent()) {
        Pais pais = opt.get();
        return pais;
      } else {
        throw new ErrorServicio("No se encontro el país solicitado");
      }
    } catch (Exception e) {
      throw new ErrorServicio("Error del sistema");
    }
  }

  @Transactional
  public Pais buscarPaisPorNombre(String nombre) throws ErrorServicio {
    try {
      Pais pais = repositorioPais.findByName(nombre);
      if (pais != null) {
        return pais;
      } else {
        throw new ErrorServicio("No se encontro el país solicitado");
      }
    } catch (Exception e) {
      throw new ErrorServicio("Error del sistema");
    }
  }

  // Escritura

  @Transactional
  public void crearPais(String nombre) throws ErrorServicio {
    validar(nombre);
    try{
      if (repositorioPais.findByName(nombre) != null) {
        throw new ErrorServicio("El país ya se encuentra registrado");
      }
      Pais pais = new Pais();
      pais.setNombre(nombre);
      pais.setEliminado(false);
      repositorioPais.save(pais);
    }catch (Exception e) {
      throw new ErrorServicio("Error del sistema");
    }
  }

  @Transactional
  public Pais modificarPais(String nombre, Long id) throws ErrorServicio {
    validar(nombre);
    try{
      if (id == null) {
        throw new ErrorServicio("El id del país no puede ser nulo");
      }
      Pais pais = buscarPais(id);
      if (pais == null) {
        throw new ErrorServicio("No se encontro el país solicitado");
      }
      pais.setNombre(nombre);
      return repositorioPais.save(pais);
    }catch (Exception e) {
      throw new ErrorServicio("Error del sistema");
    }
  }

  // Eliminacion

  @Transactional
  public void eliminarPais(Long id) throws ErrorServicio {
    try{
      if (id == null) {
        throw new ErrorServicio("El id del país no puede ser nulo");
      }
      Pais pais = buscarPais(id);
      if (pais == null) {
        throw new ErrorServicio("No se encontro el país solicitado");
      }
      pais.setEliminado(true);
      repositorioPais.save(pais);
    }catch (Exception e) {
      throw new ErrorServicio("Error del sistema");
    }
  }

  // Validacion

  private void validar(String nombre) throws ErrorServicio {
    if (nombre == null || nombre.isEmpty()) {
      throw new ErrorServicio("El nombre del país no puede ser nulo o vacío");
    }
  }

}
