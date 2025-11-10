package com.practica.ej1b.business.logic.service;

import com.practica.ej1b.business.domain.entity.Pais;
import com.practica.ej1b.business.domain.entity.Provincia;
import com.practica.ej1b.business.persistence.repository.PaisRepositorio;
import com.practica.ej1b.business.persistence.repository.ProvinciaRepositorio;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
public class ProvinciaServicio {

  @Autowired
  private ProvinciaRepositorio provinciaRepositorio;

  @Autowired
  private PaisRepositorio paisRepositorio;

  // Busqueda

  @Transactional
  public List<Provincia> listarProvincia() throws Exception{
    try{
      return provinciaRepositorio.findAll();
    } catch (Exception e){
      e.printStackTrace();
      throw new Exception("Error al listar las provincias");
    }
  }

  @Transactional
  public Collection<Provincia> listarProvinciaActivo() throws Exception{
    try{
      return provinciaRepositorio.findAllByEliminadoFalse();
    } catch (Exception e){
      e.printStackTrace();
      throw new Exception("Error al listar las provincias");
    }
  }

  @Transactional
  public Provincia buscarProvincia(String id) throws Exception{
    Optional<Provincia> respuesta = provinciaRepositorio.findById(id);
    if (respuesta.isEmpty()) {
      throw new Exception("No se encontró la provincia solicitada");
    }
    return respuesta.get();
  }

  @Transactional
  public Provincia buscarProvinciaPorNombre(String nombre) throws Exception {
    Provincia provincia = provinciaRepositorio.findProvinciaByNombre(nombre);
    if (provincia == null) {
      throw new Exception("No se encontró la provincia solicitada");
    }
    return provincia;
  }

  // Escritura

  @Transactional
  public void crearProvincia(String nombre, String paisId) throws Exception {
    validar(nombre, paisId);
    Pais pais = paisRepositorio.findById(paisId).orElseThrow(() -> new Exception("No se encontró el país solicitado"));

    Provincia provincia = Provincia.builder().
      nombre(nombre).
      eliminado(false).
      pais(pais).
      build();
    provinciaRepositorio.save(provincia);
  }

  @Transactional
  public void modificarProvincia(String id, String nombre, String paisId) throws Exception {
    validar(nombre, paisId);

    Optional<Provincia> respuesta = provinciaRepositorio.findById(id);
    if (respuesta.isEmpty()) {
      throw new Exception("No se encontró la provincia solicitada");
    }
    Pais pais = paisRepositorio.findById(paisId).orElseThrow(() -> new Exception("No se encontró el país solicitado"));

    Provincia provincia = respuesta.get();
    provincia.setNombre(nombre);
    provincia.setPais(pais);
    provinciaRepositorio.save(provincia);
  }

  @Transactional
  public void eliminarProvincia(String id) throws Exception {
    Optional<Provincia> respuesta = provinciaRepositorio.findById(id);
    if (respuesta.isEmpty()) {
      throw new Exception("No se encontró la provincia solicitada");
    }
    Provincia provincia = respuesta.get();
    provincia.setEliminado(true);
    provinciaRepositorio.save(provincia);
  }

  private void validar(String nombre, String idPais) throws Exception {
    if (nombre == null || nombre.isEmpty()) {
      throw new Exception("El nombre de la provincia no puede ser nulo o vacio");
    }
    if (idPais == null || idPais.isEmpty()) {
      throw new Exception("El id del país no puede ser nulo o vacio");
    }
  }
}
