package com.sport.proyecto.servicios;

import com.sport.proyecto.entidades.Direccion;
import com.sport.proyecto.entidades.Empresa;
import com.sport.proyecto.entidades.Sucursal;
import com.sport.proyecto.errores.ErrorServicio;
import com.sport.proyecto.repositorios.DireccionRepositorio;
import com.sport.proyecto.repositorios.SucursalRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class SucursalServicio {

  @Autowired
  private SucursalRepositorio sucursalRepositorio;

  @Autowired
  private DireccionServicio direccionServicio;

  @Autowired
  private EmpresaServicio empresaServicio;

  // Busqueda

  @Transactional
  public List<Sucursal> listarSucursal() throws ErrorServicio {
    try{
      List<Sucursal> sucursales = sucursalRepositorio.findAll();
      if (sucursales.isEmpty()) {
        throw new ErrorServicio("No existen sucursales registradas");
      }
      return sucursales;
    }catch (Exception e){
      e.printStackTrace();
      throw new ErrorServicio("Error del sistema: " + e.getMessage());
    }
  }

  @Transactional
  public List<Sucursal> listarSucursalActivas() throws ErrorServicio {
    try{
      List<Sucursal> sucursales = sucursalRepositorio.findAllActives();
      if (sucursales.isEmpty()) {
        throw new ErrorServicio("No existen sucursales registradas");
      }
      return sucursales;
    }catch (Exception e){
      e.printStackTrace();
      throw new ErrorServicio("Error del sistema: " + e.getMessage());
    }
  }

  @Transactional
  public Sucursal buscarSucursal(String id) throws ErrorServicio {
    try{
      Optional<Sucursal> opt = sucursalRepositorio.findById(id);
      if (opt.isPresent()) {
        return opt.get();
      } else {
        throw new ErrorServicio("No se encontro la sucursal solicitada");
      }
    }catch (Exception e){
      e.printStackTrace();
      throw new ErrorServicio("Error del sistema: " + e.getMessage());
    }
  }

  @Transactional
  public Sucursal buscarSucursalPorNombre(String nombre) throws ErrorServicio {
    try{
      Sucursal sucursal = sucursalRepositorio.findByName(nombre);
      if (sucursal == null) {
        throw new ErrorServicio("No se encontro la sucursal solicitada");
      }
      return sucursal;
    }catch (Exception e){
      e.printStackTrace();
      throw new ErrorServicio("Error del sistema: " + e.getMessage());
    }
  }

  // Escritura

  @Transactional
  public void crearSucursal(String nombre, String empresaId, String direccionId) throws ErrorServicio {
    validar(nombre, empresaId, direccionId);
    try{
      if (sucursalRepositorio.findByName(nombre) != null) {
        throw new ErrorServicio("Ya existe una sucursal con el nombre ingresado");
      }
      Direccion direccion = direccionServicio.buscarDireccion(direccionId);
      if (direccion == null) {
        throw new ErrorServicio("No existe la direccion ingresada");
      }
      Empresa empresa = empresaServicio.buscarEmpresa(empresaId);
      if (empresa == null) {
        throw new ErrorServicio("No existe la empresa ingresada");
      }
      Sucursal sucursal = new Sucursal();
      sucursal.setId(UUID.randomUUID().toString());
      sucursal.setNombre(nombre);
      sucursal.setEmpresa(empresa);
      sucursal.setDireccion(direccion);
      sucursal.setEliminado(false);
      sucursalRepositorio.save(sucursal);
    }catch (Exception e){
      e.printStackTrace();
      throw new ErrorServicio("Error del sistema: " + e.getMessage());
    }
  }

  @Transactional
  public void modificarSucursal(String id, String nombre, String empresaId, String direccionId) throws ErrorServicio {
    validar(nombre, empresaId, direccionId);
    try{
      if (id == null || id.isEmpty()) {
        throw new ErrorServicio("El id de la sucursal no puede ser nulo o estar vacio");
      }

      Sucursal sucursal = buscarSucursal(id);
      if (sucursal == null) {
        throw new ErrorServicio("No se encontro la sucursal solicitada");
      }
      if (direccionServicio.buscarDireccion(direccionId) == null) {
        throw new ErrorServicio("No existe la direccion ingresada");
      }
      if (empresaServicio.buscarEmpresa(empresaId) == null) {
        throw new ErrorServicio("No existe la empresa ingresada");
      }
      sucursal.setNombre(nombre);
      sucursal.setEmpresa(empresaServicio.buscarEmpresa(empresaId));
      sucursal.setDireccion(direccionServicio.buscarDireccion(direccionId));
      sucursalRepositorio.save(sucursal);
    }catch (Exception e){
      e.printStackTrace();
      throw new ErrorServicio("Error del sistema: " + e.getMessage());
    }
  }

  // Eliminacion

  @Transactional
  public void eliminarSucursal(String id) throws ErrorServicio {
    try{
      if (id == null || id.isEmpty()) {
        throw new ErrorServicio("El id de la sucursal no puede ser nulo o estar vacio");
      }
      Sucursal sucursal = buscarSucursal(id);
      if (sucursal == null) {
        throw new ErrorServicio("No se encontro la sucursal solicitada");
      }
      sucursal.setEliminado(true);
      sucursalRepositorio.save(sucursal);
    }catch (Exception e){
      e.printStackTrace();
      throw new ErrorServicio("Error del sistema: " + e.getMessage());
    }
  }

  // Validacion

  private void validar(String nombre, String empresaId, String direccionId) throws ErrorServicio {
    if (nombre == null || nombre.isEmpty()) {
      throw new ErrorServicio("El nombre de la sucursal no puede ser nulo o estar vacio");
    }
    if (empresaId == null || empresaId.isEmpty()) {
      throw new ErrorServicio("La empresa de la sucursal no puede ser nula o estar vacia");
    }
    if (direccionId == null || direccionId.isEmpty()) {
      throw new ErrorServicio("La direccion de la sucursal no puede ser nula o estar vacia");
    }
  }

}
