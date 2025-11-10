package com.practica.ej1b.business.logic.service;

import com.practica.ej1b.business.domain.entity.Departamento;
import com.practica.ej1b.business.domain.entity.Localidad;
import com.practica.ej1b.business.persistence.repository.DepartamentoRepositorio;
import com.practica.ej1b.business.persistence.repository.LocalidadRepositorio;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
public class LocalidadServicio {

  @Autowired
  private LocalidadRepositorio localidadRepositorio;

  @Autowired
  private DepartamentoRepositorio departamentoRepositorio;

  // Busqueda

  @Transactional
  public List<Localidad> listarLocalidad() throws Exception {
    try {
      return localidadRepositorio.findAll();
    } catch (Exception e) {
      e.printStackTrace();
      throw new Exception("Error al listar las localidades");
    }
  }

  @Transactional
  public Collection<Localidad> listarLocalidadActivo() throws Exception {
    try {
      return localidadRepositorio.findAllByEliminadoFalse();
    } catch (Exception e) {
      e.printStackTrace();
      throw new Exception("Error al listar las localidades");
    }
  }

  @Transactional
  public Localidad buscarLocalidad(String id) throws Exception {
    return localidadRepositorio.findById(id).orElseThrow(() -> new Exception("No se encontró la localidad solicitada"));
  }

  @Transactional
  public Localidad buscarLocalidadPorNombre(String nombre) throws Exception {
    Localidad localidad = localidadRepositorio.findLocalidadByNombre(nombre);
    if (localidad == null) {
      throw new Exception("No se encontró la localidad solicitada");
    }
    return localidad;
  }

  // Escritura

  @Transactional
  public void crearLocalidad(String nombre, String codigoPostal, String departamentoId) throws Exception {
    validar(nombre, codigoPostal, departamentoId);
    try {
      Departamento departamento = departamentoRepositorio.findById(departamentoId)
          .orElseThrow(() -> new Exception("No se encontró el departamento solicitado"));
      Localidad localidad = Localidad.builder()
          .nombre(nombre)
          .codigoPostal(codigoPostal)
          .departamento(departamento)
          .eliminado(false)
          .build();
      localidadRepositorio.save(localidad);
    } catch (Exception e) {
      e.printStackTrace();
      throw new Exception("Error al crear la localidad");
    }
  }

  @Transactional
  public void modificarLocalidad(String id, String nombre, String codigoPostal, String departamentoId) throws Exception {
    validar(nombre, codigoPostal, departamentoId);
    try {
      Localidad localidad = buscarLocalidad(id);
      Departamento departamento = departamentoRepositorio.findById(departamentoId)
          .orElseThrow(() -> new Exception("No se encontró el departamento solicitado"));
      localidad.setNombre(nombre);
      localidad.setCodigoPostal(codigoPostal);
      localidad.setDepartamento(departamento);
      localidadRepositorio.save(localidad);
    } catch (Exception e) {
      e.printStackTrace();
      throw new Exception("Error al modificar la localidad");
    }
  }

  @Transactional
  public void eliminarLocalidad(String id) throws Exception {
    try {
      Localidad localidad = buscarLocalidad(id);
      localidad.setEliminado(true);
      localidadRepositorio.save(localidad);
    } catch (Exception e) {
      e.printStackTrace();
      throw new Exception("Error al eliminar la localidad");
    }
  }

  private void validar(String nombre, String codigoPostal, String departamentoId) throws Exception {
    if (nombre == null || nombre.isEmpty()) {
      throw new Exception("El nombre de la localidad no puede ser nulo o vacío");
    }
    if (codigoPostal == null || codigoPostal.isEmpty()) {
      throw new Exception("El código postal de la localidad no puede ser nulo o vacío");
    }
    if (departamentoId == null || departamentoId.isEmpty()) {
      throw new Exception("El ID del departamento no puede ser nulo o vacío");
    }
  }

}
