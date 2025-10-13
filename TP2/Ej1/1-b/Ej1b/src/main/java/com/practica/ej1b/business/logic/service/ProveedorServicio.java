package com.practica.ej1b.business.logic.service;

import com.practica.ej1b.business.domain.entity.Proveedor;
import com.practica.ej1b.business.persistence.repository.DireccionRepositorio;
import com.practica.ej1b.business.persistence.repository.LocalidadRepositorio;
import com.practica.ej1b.business.persistence.repository.ProveedorRepositorio;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
public class ProveedorServicio {

  @Autowired
  private ProveedorRepositorio proveedorRepositorio;

  @Autowired
  private DireccionRepositorio direccionRepositorio;

  @Autowired
  private LocalidadRepositorio localidadRepositorio;

  // Busqueda

  @Transactional
  public List<Proveedor> listarProveedor() throws Exception{
    try {
      return proveedorRepositorio.findAll();
    } catch (Exception e) {
      e.printStackTrace();
      throw new Exception("Error al listar los proveedores");
    }
  }

  @Transactional
  public Collection<Proveedor> listarProveedorActivo() throws Exception{
    try {
      return proveedorRepositorio.findAllByEliminadoFalse();
    } catch (Exception e) {
      e.printStackTrace();
      throw new Exception("Error al listar los proveedores");
    }
  }

  @Transactional
  public Proveedor buscarProveedor(String id) throws Exception {
    return proveedorRepositorio.findById(id).orElseThrow(() -> new Exception("No se encontro el proveedor solicitado"));
  }

  @Transactional
  public Proveedor buscarProveedorPorNombreYApellido(String nombre, String apellido) throws Exception {
    try{
      if (nombre == null || nombre.isEmpty()) {
        throw new Exception("El nombre no puede ser nulo o estar vacio");
      }
      if (apellido == null || apellido.isEmpty()) {
        throw new Exception("El apellido no puede ser nulo o estar vacio");
      }
      Proveedor proveedor = proveedorRepositorio.findByNombreAndApellido(nombre, apellido);
      if (proveedor == null) {
        throw new Exception("No se encontro el proveedor solicitado");
      }
      return proveedor;
    } catch (Exception e) {
      e.printStackTrace();
      throw new Exception("Error al buscar el proveedor por nombre");
    }
  }

  @Transactional
  public Proveedor buscarProveedorPorCuit(String cuit) throws Exception {
    try{
      if (cuit == null || cuit.isEmpty()) {
        throw new Exception("El cuit no puede ser nulo o estar vacio");
      }
      Proveedor proveedor = proveedorRepositorio.findByCuit(cuit);
      if (proveedor == null) {
        throw new Exception("No se encontro el proveedor solicitado");
      }
      return proveedor;
    } catch (Exception e) {
      e.printStackTrace();
      throw new Exception("Error al buscar el proveedor por cuit");
    }
  }

  // Escritura

  @Transactional
  public void crearProveedor(String nombre, String apellido, String cuit, String telefono, String correoElectronico, String direccionId) throws Exception {
    validar(nombre, apellido, cuit, telefono, correoElectronico, direccionId);
    try {
      Proveedor proveedor = new Proveedor();
      proveedor.setNombre(nombre);
      proveedor.setApellido(apellido);
      proveedor.setCuit(cuit);
      proveedor.setTelefono(telefono);
      proveedor.setCorreoElectronico(correoElectronico);
      proveedor.setDireccion(direccionRepositorio.findById(direccionId).orElseThrow(() -> new Exception("No se encontro la direccion solicitada")));
      proveedor.setEliminado(false);
      proveedorRepositorio.save(proveedor);
    } catch (Exception e) {
      e.printStackTrace();
      throw new Exception("Error al crear el proveedor: " + e.getMessage());
    }
  }

  @Transactional
  public void modificarProveedor(String id, String nombre, String apellido, String cuit, String telefono, String correoElectronico, String direccionId) throws Exception {
    validar(nombre, apellido, cuit, telefono, correoElectronico, direccionId);
    try {
      Proveedor proveedor = proveedorRepositorio.findById(id).orElseThrow(() -> new Exception("No se encontro el proveedor solicitado"));
      proveedor.setNombre(nombre);
      proveedor.setApellido(apellido);
      proveedor.setCuit(cuit);
      proveedor.setTelefono(telefono);
      proveedor.setCorreoElectronico(correoElectronico);
      proveedor.setDireccion(direccionRepositorio.findById(direccionId).orElseThrow(() -> new Exception("No se encontro la direccion solicitada")));
      proveedorRepositorio.save(proveedor);
    } catch (Exception e) {
      e.printStackTrace();
      throw new Exception("Error al modificar el proveedor: " + e.getMessage());
    }
  }

  @Transactional
  public void eliminarProveedor(String id) throws Exception {
    try {
      Proveedor proveedor = proveedorRepositorio.findById(id).orElseThrow(() -> new Exception("No se encontro el proveedor solicitado"));
      proveedor.setEliminado(true);
      proveedorRepositorio.save(proveedor);
    } catch (Exception e) {
      e.printStackTrace();
      throw new Exception("Error al eliminar el proveedor: " + e.getMessage());
    }
  }

  private void validar(String nombre, String apellido, String cuit, String telefono, String correoElectronico, String direccionId) throws Exception {
    if (nombre == null || nombre.isEmpty()) {
      throw new Exception("El nombre no puede ser nulo o estar vacio");
    }
    if (apellido == null || apellido.isEmpty()) {
      throw new Exception("El apellido no puede ser nulo o estar vacio");
    }
    if (cuit == null || cuit.isEmpty()) {
      throw new Exception("El cuit no puede ser nulo o estar vacio");
    }
    if (telefono == null || telefono.isEmpty()) {
      throw new Exception("El telefono no puede ser nulo o estar vacio");
    }
    if (correoElectronico == null || correoElectronico.isEmpty()) {
      throw new Exception("El correo electronico no puede ser nulo o estar vacio");
    }
    if (direccionId == null || direccionId.isEmpty()) {
      throw new Exception("La direccion no puede ser nula o estar vacia");
    }
  }

}
