package com.practica.ej1b.business.logic.service;

import com.practica.ej1b.business.domain.entity.Direccion;
import com.practica.ej1b.business.domain.entity.Localidad;
import com.practica.ej1b.business.persistence.repository.DireccionRepositorio;
import com.practica.ej1b.business.persistence.repository.LocalidadRepositorio;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
public class DireccionServicio {

  @Autowired
  private DireccionRepositorio direccionRepositorio;

  @Autowired
  private LocalidadRepositorio localidadRepositorio;

  // Busqueda

  @Transactional
  public List<Direccion> listarDireccion() throws Exception{
    try {
      return direccionRepositorio.findAll();
    } catch (Exception e) {
      e.printStackTrace();
      throw new Exception("Error al listar las direcciones");
    }
  }

  @Transactional
  public Collection<Direccion> listarDireccionActivo() throws Exception{
    try {
      return direccionRepositorio.findAllByEliminadoFalse();
    } catch (Exception e) {
      e.printStackTrace();
      throw new Exception("Error al listar las direcciones");
    }
  }

  @Transactional
  public Direccion buscarDireccion(String id) throws Exception {
    return direccionRepositorio.findById(id).orElseThrow(() -> new Exception("No se encontro la direccion solicitada"));
  }

  @Transactional
  public Direccion buscarDireccionPorCalleYNumeracion(String calle, String numeracion) throws Exception {
    try{
      if (calle == null || calle.isEmpty()) {
        throw new Exception("La calle no puede ser nula o estar vacia");
      }
      if (numeracion == null || numeracion.isEmpty()) {
        throw new Exception("La numeracion no puede ser nula o estar vacia");
      }
      Direccion direccion = direccionRepositorio.findByCalleAndNumeracion(calle, numeracion);
      if (direccion == null) {
        throw new Exception("No se encontro la direccion solicitada");
      }
      return direccion;
    } catch (Exception e) {
      e.printStackTrace();
      throw new Exception("Error del sistema: " + e.getMessage());
    }
  }

  // Escritura

  @Transactional
  public Direccion crearDireccion(String calle, String numeracion, String barrio, String manzanaPiso, String casaDepartamento, String referencia,
                                  String idLocalidad, String latitud, String longitud) throws Exception {
    validar(calle, numeracion, barrio, manzanaPiso, casaDepartamento, referencia, idLocalidad, latitud, longitud);
    try {
      Localidad localidad = localidadRepositorio.findById(idLocalidad).orElseThrow(() -> new Exception("No se encontro la localidad solicitada"));

      Direccion direccion = new Direccion();
      direccion.setCalle(calle);
      direccion.setNumeracion(numeracion);
      direccion.setBarrio(barrio);
      direccion.setManzanaPiso(manzanaPiso);
      direccion.setCasaDepartamento(casaDepartamento);
      direccion.setReferencia(referencia);
      direccion.setLatitud(latitud);
      direccion.setLongitud(longitud);
      direccion.setEliminado(false);
      direccion.setLocalidad(localidad);
      direccionRepositorio.save(direccion);
      return direccion;
    } catch (Exception e) {
      e.printStackTrace();
      throw new Exception("Error del sistema: " + e.getMessage());
    }
  }

  @Transactional
  public Direccion modificarDireccion(String id, String calle, String numeracion, String barrio, String manzanaPiso, String casaDepartamento, String referencia,
                                      String idLocalidad, String latitud, String longitud) throws Exception {
    validar(calle, numeracion, barrio, manzanaPiso, casaDepartamento, referencia, idLocalidad, latitud, longitud);
    try {
      Direccion direccion = buscarDireccion(id);
      if (direccion == null) {
        throw new Exception("No se encontro la direccion solicitada");
      }
      Localidad localidad = localidadRepositorio.findById(idLocalidad).orElseThrow(() -> new Exception("No se encontro la localidad solicitada"));

      direccion.setCalle(calle);
      direccion.setNumeracion(numeracion);
      direccion.setBarrio(barrio);
      direccion.setManzanaPiso(manzanaPiso);
      direccion.setCasaDepartamento(casaDepartamento);
      direccion.setReferencia(referencia);
      direccion.setLatitud(latitud);
      direccion.setLongitud(longitud);
      direccion.setLocalidad(localidad);
      direccionRepositorio.save(direccion);
      return direccion;
    } catch (Exception e) {
      e.printStackTrace();
      throw new Exception("Error del sistema: " + e.getMessage());
    }
  }

  @Transactional
  public void eliminarDireccion(String id) throws Exception {
    try {
      Direccion direccion = buscarDireccion(id);
      if (direccion == null) {
        throw new Exception("No se encontro la direccion solicitada");
      }
      direccion.setEliminado(true);
      direccionRepositorio.save(direccion);
    } catch (Exception e) {
      e.printStackTrace();
      throw new Exception("Error del sistema: " + e.getMessage());
    }
  }

  // Validacion

  private void validar(String calle, String numeracion, String barrio, String manzanaPiso, String casaDepartamento,
                       String referencia, String idLocalidad, String latitud, String longitud) throws Exception {
    if (calle == null || calle.isEmpty()) {
      throw new Exception("La calle no puede ser nula o estar vacia");
    }
    if (numeracion == null || numeracion.isEmpty()) {
      throw new Exception("La numeracion no puede ser nula o estar vacia");
    }
    if (barrio == null || barrio.isEmpty()) {
      throw new Exception("El barrio no puede ser nulo o estar vacio");
    }
    if (manzanaPiso == null || manzanaPiso.isEmpty()) {
      throw new Exception("La manzana/piso no puede ser nulo o estar vacio");
    }
    if (casaDepartamento == null || casaDepartamento.isEmpty()) {
      throw new Exception("La casa/departamento no puede ser nulo o estar vacio");
    }
    if (idLocalidad == null || idLocalidad.isEmpty()) {
      throw new Exception("La localidad no puede ser nula o estar vacia");
    }
    if ( referencia.length() > 100) {
      throw new Exception("La referencia no puede tener mas de 100 caracteres");
    }
    if (latitud != null && latitud.length() > 20) {
      throw new Exception("La latitud no puede tener mas de 20 caracteres");
    }
    if (longitud != null && longitud.length() > 20) {
      throw new Exception("La longitud no puede tener mas de 20 caracteres");
    }
  }
}
