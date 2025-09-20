package com.sport.proyecto.servicios;

import com.sport.proyecto.entidades.Empresa;
import com.sport.proyecto.errores.ErrorServicio;
import com.sport.proyecto.repositorios.EmpresaRepositorio;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmpresaServicio {

  @Autowired
  private EmpresaRepositorio empresaRepositorio;

  // Busqueda

  @Transactional
  public List<Empresa> listarEmpresa() throws ErrorServicio {
    try {
      List<Empresa> empresas = empresaRepositorio.findAll();
      if (empresas.isEmpty()) {
        throw new ErrorServicio("No hay empresas cargadas");
      }
      return empresas;
    } catch (Exception e) {
      throw new ErrorServicio("Error del sistema");
    }
  }

  @Transactional
  public List<Empresa> listarEmpresaActiva() throws ErrorServicio {
    try {
      List<Empresa> empresas = empresaRepositorio.findAllActives();
      if (empresas.isEmpty()) {
        throw new ErrorServicio("No hay empresas cargadas");
      }
      return empresas;
    } catch (Exception e) {
      throw new ErrorServicio("Error del sistema");
    }
  }

  @Transactional
  public Empresa buscarEmpresa(Long id) throws ErrorServicio {
    try{
      Optional<Empresa> opt = empresaRepositorio.findById(id);
      if (opt.isPresent()) {
        return opt.get();
      } else {
        throw new ErrorServicio("No se encontro la empresa solicitada");
      }
    }catch (Exception e){
      throw new ErrorServicio("Error del sistema");
    }
  }

  @Transactional
  public Empresa buscarPorNombre(String nombre) throws ErrorServicio {
    try {
      Empresa empresa = empresaRepositorio.findByName(nombre);
      if (empresa == null) {
        throw new ErrorServicio("No se encontro la empresa solicitada");
      }
      return empresa;
    } catch (Exception e) {
      throw new ErrorServicio("Error del sistema");
    }
  }

  // Escritura

  @Transactional
  public void crearEmpresa(String nombre, String telefono, String correoElectronico) throws ErrorServicio {
    validar(nombre, telefono, correoElectronico);
    try {
      Empresa empresa = new Empresa();
      empresa.setNombre(nombre);
      empresa.setTelefono(telefono);
      empresa.setCorreoElectronico(correoElectronico);
      empresa.setEliminado(false);
      empresaRepositorio.save(empresa);
    } catch (Exception e) {
      throw new ErrorServicio("Error del sistema");
    }
  }

  @Transactional
  public void modificarEmpresa(String nombre, String telefono, String correoElectronico, Long id) throws ErrorServicio {
    validar(nombre, telefono, correoElectronico);
    try {
      Empresa empresa = buscarEmpresa(id);
      if (empresa == null) {
        throw new ErrorServicio("No se encontro la empresa solicitada");
      }
      empresa.setNombre(nombre);
      empresa.setTelefono(telefono);
      empresa.setCorreoElectronico(correoElectronico);
      empresaRepositorio.save(empresa);
    } catch (Exception e) {
      throw new ErrorServicio("Error del sistema");
    }
  }

  @Transactional
  public void eliminarEmpresa(Long id) throws ErrorServicio {
    try {
      Empresa empresa = buscarEmpresa(id);
      if (empresa == null) {
        throw new ErrorServicio("No se encontro la empresa solicitada");
      }
      empresa.setEliminado(true);
      empresaRepositorio.save(empresa);
    } catch (Exception e) {
      throw new ErrorServicio("Error del sistema");
    }
  }

  // Validacion
  private void validar(String nombre, String telefono, String correoElectronico) throws ErrorServicio {
    if (nombre == null || nombre.isEmpty()) {
      throw new ErrorServicio("El nombre no puede ser nulo o estar vacio");
    }
    if (telefono == null || telefono.isEmpty()) {
      throw new ErrorServicio("El telefono no puede ser nulo o estar vacio");
    }
    if (correoElectronico == null || correoElectronico.isEmpty()) {
      throw new ErrorServicio("El correo electronico no puede ser nulo o estar vacio");
    }
  }
}
