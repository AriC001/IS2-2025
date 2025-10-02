package com.practica.ej1b.business.logic.service;

import com.practica.ej1b.business.domain.entity.Pais;
import com.practica.ej1b.business.persistence.repository.PaisRepositorio;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
public class PaisServicio {

  @Autowired
  private PaisRepositorio paisRepositorio;

  // Busqueda

  @Transactional
  public List<Pais> listarPais() throws Exception{
    try{
      return paisRepositorio.findAll();
    } catch (Exception e){
      e.printStackTrace();
      throw new Exception("Error al listar los paises");
    }
  }

  @Transactional
  public Collection<Pais> listarPaisActivo() throws Exception{
    try{
      return paisRepositorio.findAllByEliminadoFalse();
    } catch (Exception e){
      e.printStackTrace();
      throw new Exception("Error al listar los paises");
    }
  }

  @Transactional
  public Pais buscarPais(String id) throws Exception{
    Optional<Pais> respuesta = paisRepositorio.findById(id);
    if (respuesta.isEmpty()) {
      throw new Exception("No se encontro el pais solicitado");
    }
    return respuesta.get();
  }

  @Transactional
  public Pais buscarPaisPorNombre(String nombre) throws Exception {
    Pais pais = paisRepositorio.findPaisByNombre(nombre);
    if (pais == null) {
      throw new Exception("No se encontro el pais solicitado");
    }
    return pais;
  }

  // Escritura

  @Transactional
  public void crearPais(String nombre) throws Exception {
    if (nombre == null || nombre.isEmpty()) {
      throw new Exception("El nombre del pais no puede ser nulo o vacio");
    }
    Pais pais = Pais.builder().
      nombre(nombre).
      eliminado(false).
      build();
    paisRepositorio.save(pais);
  }

  @Transactional
  public Pais modificarPais(String nombre, String id) throws Exception {
    Optional<Pais> respuesta = paisRepositorio.findById(id);
    if (respuesta.isEmpty()) {
      throw new Exception("No se encontro el pais solicitado");
    }
    Pais pais = respuesta.get();
    pais.setNombre(nombre);
    return paisRepositorio.save(pais);
  }

  // Eliminacion

  @Transactional
  public void eliminarPais(String id) throws Exception {
    Optional<Pais> respuesta = paisRepositorio.findById(id);
    if (respuesta.isEmpty()) {
      throw new Exception("No se encontro el pais solicitado");
    }
    Pais pais = respuesta.get();
    pais.setEliminado(true);
    paisRepositorio.save(pais);
  }


}
