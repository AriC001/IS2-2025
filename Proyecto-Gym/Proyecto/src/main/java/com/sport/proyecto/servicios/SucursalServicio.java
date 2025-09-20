package com.sport.proyecto.servicios;

import com.sport.proyecto.entidades.Sucursal;
import com.sport.proyecto.errores.ErrorServicio;
import com.sport.proyecto.repositorios.SucursalRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

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
      throw new ErrorServicio("Error del sistema");
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
      throw new ErrorServicio("Error del sistema");
    }
  }

  @Transactional
  public Sucursal buscarSucursal(Long id) throws ErrorServicio {
    try{
      Optional<Sucursal> opt = sucursalRepositorio.findById(id);
      if (opt.isPresent()) {
        return opt.get();
      } else {
        throw new ErrorServicio("No se encontro la sucursal solicitada");
      }
    }catch (Exception e){
      throw new ErrorServicio("Error del sistema");
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
      throw new ErrorServicio("Error del sistema");
    }
  }

  // Escritura

  @Transactional
  public Sucursal crearSucursal(String nombre, Long empresaId, Long direccionId) throws ErrorServicio {
    validar(nombre, empresaId, direccionId);
    try{
      if (buscarSucursalPorNombre(nombre) != null) {
        throw new ErrorServicio("Ya existe una sucursal con el nombre ingresado");
      }
      if (direccionServicio.buscarDireccion(direccionId) == null) {
        throw new ErrorServicio("No existe la direccion ingresada");
      }
      if (empresaServicio.buscarEmpresa(empresaId) == null) {
        throw new ErrorServicio("No existe la empresa ingresada");
      }
      Sucursal sucursal = new Sucursal();
      sucursal.setNombre(nombre);
      sucursal.setEmpresa(empresaServicio.buscarEmpresa(empresaId));
      sucursal.setDireccion(direccionServicio.buscarDireccion(direccionId));
      sucursal.setEliminado(false);
      return sucursalRepositorio.save(sucursal);
    }catch (Exception e){
      throw new ErrorServicio("Error del sistema");
    }
  }

  @Transactional
  public Sucursal modificarSucursal(Long id, String nombre, Long empresaId, Long direccionId) throws ErrorServicio {
    validar(nombre, empresaId, direccionId);
    try{
      if (id == null || id.toString().isEmpty()) {
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
      return sucursalRepositorio.save(sucursal);
    }catch (Exception e){
      throw new ErrorServicio("Error del sistema");
    }
  }

  // Eliminacion

  @Transactional
  public void eliminarSucursal(Long id) throws ErrorServicio {
    try{
      if (id == null || id.toString().isEmpty()) {
        throw new ErrorServicio("El id de la sucursal no puede ser nulo o estar vacio");
      }
      Sucursal sucursal = buscarSucursal(id);
      if (sucursal == null) {
        throw new ErrorServicio("No se encontro la sucursal solicitada");
      }
      sucursal.setEliminado(true);
      sucursalRepositorio.save(sucursal);
    }catch (Exception e){
      throw new ErrorServicio("Error del sistema");
    }
  }

  // Validacion

  private void validar(String nombre, Long empresaId, Long direccionId) throws ErrorServicio {
    if (nombre == null || nombre.isEmpty()) {
      throw new ErrorServicio("El nombre de la sucursal no puede ser nulo o estar vacio");
    }
    if (empresaId == null || empresaId.toString().isEmpty()) {
      throw new ErrorServicio("La empresa de la sucursal no puede ser nula o estar vacia");
    }
    if (direccionId == null || direccionId.toString().isEmpty()) {
      throw new ErrorServicio("La direccion de la sucursal no puede ser nula o estar vacia");
    }
  }

}
