package com.sport.proyecto.servicios;

import com.sport.proyecto.entidades.Departamento;
import com.sport.proyecto.entidades.Localidad;
import com.sport.proyecto.errores.ErrorServicio;
import com.sport.proyecto.repositorios.LocalidadRepositorio;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LocalidadServicio {

  @Autowired
  private LocalidadRepositorio localidadRepositorio;

  @Autowired
  private DepartamentoServicio departamentoServicio;

  // Busqueda

  @Transactional
  public List<Localidad> listarLocalidad() throws ErrorServicio {
    try {
      List<Localidad> localidades = localidadRepositorio.findAll();
      if (localidades.isEmpty()) {
        throw new ErrorServicio("No hay localidades cargadas");
      }
      return localidades;
    } catch (Exception e) {
      throw new ErrorServicio("Error del sistema");
    }
  }

  @Transactional
  public List<Localidad> listarLocalidadActiva() throws ErrorServicio {
    try {
      List<Localidad> localidades = localidadRepositorio.findAllActives();
      if (localidades.isEmpty()) {
        throw new ErrorServicio("No hay localidades cargadas");
      }
      return localidades;
    } catch (Exception e) {
      throw new ErrorServicio("Error del sistema");
    }
  }

  @Transactional
  public Localidad buscarLocalidad(Long id) throws ErrorServicio {
    try {
      Optional<Localidad> opt = localidadRepositorio.findById(id);
      if (opt.isPresent()) {
        return opt.get();
      } else {
        throw new ErrorServicio("No se encontro la localidad solicitada");
      }
    } catch (Exception e) {
      throw new ErrorServicio("Error del sistema");
    }
  }

  @Transactional
  public Localidad buscarLocalidadPorNombre(String nombre) throws ErrorServicio {
    try {
      Localidad localidad = localidadRepositorio.findByName(nombre);
      if (localidad != null) {
        return localidad;
      } else {
        throw new ErrorServicio("No se encontro la localidad solicitada");
      }
    } catch (Exception e) {
      throw new ErrorServicio("Error del sistema");
    }
  }

  @Transactional
  public Localidad buscarLocalidadPorCodigoPostal(String codigoPostal) throws ErrorServicio {
    try {
      Localidad localidad = localidadRepositorio.findByPostalCode(codigoPostal);
      if (localidad != null) {
        return localidad;
      } else {
        throw new ErrorServicio("No se encontro la localidad solicitada");
      }
    } catch (Exception e) {
      throw new ErrorServicio("Error del sistema");
    }
  }

  @Transactional
  public List<Localidad> buscarLocalidadPorDepartamento(Long idDepartamento) throws ErrorServicio{
    try{
      Departamento departamento = departamentoServicio.buscarDepartamento(idDepartamento);
      if (departamento == null) {
        throw new ErrorServicio("No se encontro el departamento solicitado");
      }
      List<Localidad> localidades = localidadRepositorio.findByDepartamento(idDepartamento);
      if (localidades.isEmpty()) {
        throw new ErrorServicio("No hay localidades cargadas para el departamento seleccionado");
      }
      return localidades;
    }catch (Exception e){
      throw new ErrorServicio("Error del sistema");
    }
  }

  // Escritura

  @Transactional
  public void crearLocalidad(String nombre, String codigoPostal, Long idDepartamento) throws ErrorServicio {
    validar(nombre, codigoPostal, idDepartamento);
    try {
      Departamento departamento = departamentoServicio.buscarDepartamento(idDepartamento);
      if (departamento == null) {
        throw new ErrorServicio("No se encontro el departamento solicitado");
      }
      Localidad localidad = new Localidad();
      localidad.setNombre(nombre);
      localidad.setCodigoPostal(codigoPostal);
      localidad.setDepartamento(departamento);
      localidad.setEliminado(false);
      localidadRepositorio.save(localidad);
    } catch (Exception e) {
      throw new ErrorServicio("Error del sistema");
    }
  }

  @Transactional
  public void modificarLocalidad(Long id, String nombre, String codigoPostal, Long idDepartamento) throws ErrorServicio {
    validar(nombre, codigoPostal, idDepartamento);
    try{
      if (id == null) {
        throw new ErrorServicio("El id no puede ser nulo");
      }
      Localidad localidad = buscarLocalidad(id);
      if (localidad == null) {
        throw new ErrorServicio("No se encontro la localidad solicitada");
      }
      Departamento departamento = departamentoServicio.buscarDepartamento(idDepartamento);
      if (departamento == null) {
        throw new ErrorServicio("No se encontro el departamento solicitado");
      }
      localidad.setNombre(nombre);
      localidad.setCodigoPostal(codigoPostal);
      localidad.setDepartamento(departamento);
      localidadRepositorio.save(localidad);
    }catch (Exception e) {
      throw new ErrorServicio("Error del sistema");
    }
  }

  // Eliminacion

  @Transactional
  public void eliminarLocalidad(Long id) throws ErrorServicio {
    try{
      if (id == null) {
        throw new ErrorServicio("El id no puede ser nulo");
      }
      Localidad localidad = buscarLocalidad(id);
      if (localidad == null) {
        throw new ErrorServicio("No se encontro la localidad solicitada");
      }
      localidad.setEliminado(true);
      localidadRepositorio.save(localidad);
    }catch (Exception e) {
      throw new ErrorServicio("Error del sistema");
    }
  }

  // Validacion

  private void validar(String nombre, String codigoPostal, Long idDepartamento) throws ErrorServicio {
    if (nombre == null || nombre.isEmpty()) {
      throw new ErrorServicio("El nombre no puede ser nulo o vacio");
    }
    if (codigoPostal == null || codigoPostal.isEmpty()) {
      throw new ErrorServicio("El codigo postal no puede ser nulo o vacio");
    }
    if (idDepartamento == null || idDepartamento.toString().isEmpty()) {
      throw new ErrorServicio("El departamento no puede ser nulo o vacio");
    }
  }
}
