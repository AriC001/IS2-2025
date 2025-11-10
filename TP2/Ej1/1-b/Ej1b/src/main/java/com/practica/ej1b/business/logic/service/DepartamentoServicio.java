package com.practica.ej1b.business.logic.service;

import com.practica.ej1b.business.domain.entity.Departamento;
import com.practica.ej1b.business.domain.entity.Provincia;
import com.practica.ej1b.business.persistence.repository.DepartamentoRepositorio;
import com.practica.ej1b.business.persistence.repository.ProvinciaRepositorio;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DepartamentoServicio {

  @Autowired
  private DepartamentoRepositorio departamentoRepositorio;

  @Autowired
  private ProvinciaRepositorio provinciaRepositorio;

  // Busqueda

  @Transactional
  public List<Departamento> listarDepartamento() throws Exception {
    try {
      return departamentoRepositorio.findAll();
    } catch (Exception e) {
      e.printStackTrace();
      throw new Exception("Error al listar los departamentos");
    }
  }

  @Transactional
  public List<Departamento> listarDepartamentoActivo() throws Exception {
    try {
      return departamentoRepositorio.findAllByEliminadoFalse();
    } catch (Exception e) {
      e.printStackTrace();
      throw new Exception("Error al listar los departamentos");
    }
  }

  @Transactional
  public Departamento buscarDepartamento(String id) throws Exception {
    return departamentoRepositorio.findById(id).orElseThrow(() -> new Exception("No se encontró el departamento solicitado"));
  }

  @Transactional
  public Departamento buscarDepartamentoPorNombre(String nombre) throws Exception {
    Departamento departamento = departamentoRepositorio.findByNombre(nombre);
    if (departamento == null) {
      throw new Exception("No se encontró el departamento solicitado");
    }
    return departamento;
  }

  // Escritura

  @Transactional
  public void crearDepartamento(String nombre, String provinciaId) throws Exception {
    validar(nombre, provinciaId);
    try {
      Provincia provincia = provinciaRepositorio.findById(provinciaId).orElseThrow(() -> new Exception("No se encontró la provincia solicitada"));
      Departamento departamento = Departamento.builder()
          .nombre(nombre)
          .eliminado(false)
          .provincia(provincia)
          .build();
      departamentoRepositorio.save(departamento);
    } catch (Exception e) {
      e.printStackTrace();
      throw new Exception("Error al crear el departamento");
    }
  }

  @Transactional
  public void modificarDepartamento(String id, String nombre, String provinciaId) throws Exception {
    validar(nombre, provinciaId);
    try {
      Departamento departamento = buscarDepartamento(id);
      Provincia provincia = provinciaRepositorio.findById(provinciaId).orElseThrow(() -> new Exception("No se encontró la provincia solicitada"));
      departamento.setNombre(nombre);
      departamento.setProvincia(provincia);
      departamentoRepositorio.save(departamento);
    } catch (Exception e) {
      e.printStackTrace();
      throw new Exception("Error al modificar el departamento");
    }
  }

  @Transactional
  public void eliminarDepartamento(String id) throws Exception {
    try {
      Departamento departamento = buscarDepartamento(id);
      departamento.setEliminado(true);
      departamentoRepositorio.save(departamento);
    } catch (Exception e) {
      e.printStackTrace();
      throw new Exception("Error al eliminar el departamento");
    }
  }

  private void validar(String nombre, String provinciaId) throws Exception {
    if (nombre == null || nombre.isEmpty()) {
      throw new Exception("El nombre no puede ser nulo o vacío");
    }
    if (provinciaId == null || provinciaId.isEmpty()) {
      throw new Exception("La provincia no puede ser nula o vacía");
    }
  }

}
