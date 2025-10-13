package com.practica.ej1b.business.logic.service;

import com.practica.ej1b.business.domain.entity.Direccion;
import com.practica.ej1b.business.domain.entity.Empresa;
import com.practica.ej1b.business.domain.entity.Localidad;
import com.practica.ej1b.business.persistence.repository.DireccionRepositorio;
import com.practica.ej1b.business.persistence.repository.EmpresaRepositorio;
import com.practica.ej1b.business.persistence.repository.LocalidadRepositorio;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
public class EmpresaServicio {

  @Autowired
  private EmpresaRepositorio empresaRepositorio;

  @Autowired
  private DireccionRepositorio direccionRepositorio;

  @Autowired
  private LocalidadRepositorio localidadRepositorio;

  // Busqueda

  @Transactional
  public List<Empresa> listarEmpresa() throws Exception{
    try {
      return empresaRepositorio.findAll();
    } catch (Exception e) {
      e.printStackTrace();
      throw new Exception("Error al listar las empresas");
    }
  }

  @Transactional
  public Collection<Empresa> listarEmpresaActivo() throws Exception{
    try {
      return empresaRepositorio.findAllByEliminadoFalse();
    } catch (Exception e) {
      e.printStackTrace();
      throw new Exception("Error al listar las empresas");
    }
  }

  @Transactional
  public Empresa buscarEmpresa(String id) throws Exception {
    return empresaRepositorio.findById(id).orElseThrow(() -> new Exception("No se encontro la empresa solicitada"));
  }

  @Transactional
  public Empresa buscarEmpresaPorRazonSocial(String razonSocial) throws Exception {
    try{
      if (razonSocial == null || razonSocial.isEmpty()) {
        throw new Exception("La razon social no puede ser nula o estar vacia");
      }
      Empresa empresa = empresaRepositorio.findByRazonSocial(razonSocial);
      if (empresa == null) {
        throw new Exception("No se encontro la empresa solicitada");
      }
      return empresa;
    } catch (Exception e) {
      e.printStackTrace();
      throw new Exception("Error al buscar la empresa por razon social");
    }
  }

  // Escritura

  @Transactional
  public void crearEmpresa(String razonSocial, String direccionId) throws Exception {
    validar(razonSocial, direccionId);
    try {
      Direccion direccion = direccionRepositorio.findById(direccionId).orElseThrow(() -> new Exception("No se encontro la direccion solicitada"));
      Empresa empresa = new Empresa();
      empresa.setRazonSocial(razonSocial);
      empresa.setDireccion(direccion);
      empresa.setEliminado(false);
      empresaRepositorio.save(empresa);
    } catch (Exception e) {
      e.printStackTrace();
      throw new Exception("Error al crear la empresa: " + e.getMessage());
    }
  }

  @Transactional
  public void crearEmpresaConDireccion(String razonSocial,
                                       String calle, String numeracion, String barrio,
                                       String manzanaPiso, String casaDepartamento, String referencia,
                                       String localidadId, String latitud, String longitud) throws Exception{

    validar(razonSocial, localidadId);
    try{
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
        throw new Exception("La manzana/piso no puede ser nula o estar vacia");
      }
      if (casaDepartamento == null || casaDepartamento.isEmpty()) {
        throw new Exception("La casa/departamento no puede ser nula o estar vacia");
      }
      if (latitud == null || latitud.isEmpty()) {
        throw new Exception("La latitud no puede ser nula o estar vacia");
      }
      if (longitud == null || longitud.isEmpty()) {
        throw new Exception("La longitud no puede ser nula o estar vacia");
      }

      Localidad localidad = localidadRepositorio.findById(localidadId).orElseThrow(() -> new Exception("No se encontro la localidad solicitada"));
      Direccion direccion = new Direccion();
      direccion.setCalle(calle);
      direccion.setNumeracion(numeracion);
      direccion.setBarrio(barrio);
      direccion.setManzanaPiso(manzanaPiso);
      direccion.setCasaDepartamento(casaDepartamento);
      direccion.setReferencia(referencia);
      direccion.setLocalidad(localidad);
      direccion.setLatitud(latitud);
      direccion.setLongitud(longitud);
      direccion.setEliminado(false);
      direccionRepositorio.save(direccion);

      Empresa empresa = new Empresa();
      empresa.setRazonSocial(razonSocial);
      empresa.setDireccion(direccion);
      empresa.setEliminado(false);
      empresaRepositorio.save(empresa);
    } catch (Exception e) {
      e.printStackTrace();
      throw new Exception("Error al crear la empresa con direccion: " + e.getMessage());
    }
  }

  @Transactional
  public void modificarEmpresa(String id, String razonSocial, String direccionId) throws Exception {
    validar(razonSocial, direccionId);
    try {
      Empresa empresa = buscarEmpresa(id);
      Direccion direccion = direccionRepositorio.findById(direccionId).orElseThrow(() -> new Exception("No se encontro la direccion solicitada"));
      empresa.setRazonSocial(razonSocial);
      empresa.setDireccion(direccion);
      empresaRepositorio.save(empresa);
    }catch (Exception e) {
      e.printStackTrace();
      throw new Exception("Error al modificar la empresa: " + e.getMessage());
    }
  }

  @Transactional
  public void modificarEmpresaConDireccion(String id, String razonSocial,
                                          String calle, String numeracion, String barrio,
                                          String manzanaPiso, String casaDepartamento, String referencia,
                                          String localidadId, String latitud, String longitud) throws Exception {
    validar(razonSocial, localidadId);
    try {
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
        throw new Exception("La manzana/piso no puede ser nula o estar vacia");
      }
      if (casaDepartamento == null || casaDepartamento.isEmpty()) {
        throw new Exception("La casa/departamento no puede ser nula o estar vacia");
      }
      if (latitud == null || latitud.isEmpty()) {
        throw new Exception("La latitud no puede ser nula o estar vacia");
      }
      if (longitud == null || longitud.isEmpty()) {
        throw new Exception("La longitud no puede ser nula o estar vacia");
      }

      Empresa empresa = buscarEmpresa(id);
      Direccion direccion = empresa.getDireccion();
      Localidad localidad = localidadRepositorio.findById(localidadId).orElseThrow(() -> new Exception("No se encontro la localidad solicitada"));
      direccion.setCalle(calle);
      direccion.setNumeracion(numeracion);
      direccion.setBarrio(barrio);
      direccion.setManzanaPiso(manzanaPiso);
      direccion.setCasaDepartamento(casaDepartamento);
      direccion.setReferencia(referencia);
      direccion.setLocalidad(localidad);
      direccion.setLatitud(latitud);
      direccion.setLongitud(longitud);
      direccionRepositorio.save(direccion);

      empresa.setRazonSocial(razonSocial);
      empresa.setDireccion(direccion);
      empresaRepositorio.save(empresa);
    } catch (Exception e) {
      e.printStackTrace();
      throw new Exception("Error al modificar la empresa con direccion: " + e.getMessage());
    }
  }

  @Transactional
  public void eliminarEmpresa(String id) throws Exception {
    try {
      Empresa empresa = buscarEmpresa(id);
      empresa.setEliminado(true);
      empresaRepositorio.save(empresa);
    } catch (Exception e) {
      e.printStackTrace();
      throw new Exception("Error al eliminar la empresa: " + e.getMessage());
    }
  }

  private void validar(String razonSocial, String direccionId) throws Exception {
    if (razonSocial == null || razonSocial.isEmpty()) {
      throw new Exception("La razon social no puede ser nula o estar vacia");
    }
    if (direccionId == null || direccionId.isEmpty()) {
      throw new Exception("La direccion no puede ser nula o estar vacia");
    }
  }
}
