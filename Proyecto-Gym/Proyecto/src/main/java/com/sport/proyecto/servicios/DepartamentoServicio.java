package com.sport.proyecto.servicios;

import com.sport.proyecto.entidades.Departamento;
import com.sport.proyecto.entidades.Provincia;
import com.sport.proyecto.errores.ErrorServicio;
import com.sport.proyecto.repositorios.DepartamentoRepositorio;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DepartamentoServicio {
  @Autowired
  private DepartamentoRepositorio repositorioDepartamento;

  @Autowired
  private ProvinciaServicio provinciaServicio;

  // Busqueda

  @Transactional
  public List<Departamento> listarDepartamento() throws ErrorServicio {
    try {
      return repositorioDepartamento.findAll();
    }catch (Exception e){
      throw new ErrorServicio("Error del sistema");
    }
  }

  @Transactional
  public List<Departamento> listarDepartamentoActivo() throws ErrorServicio {
    try{
      return repositorioDepartamento.findAllActives();
    }catch (Exception e){
      throw new ErrorServicio("Error del sistema");
    }
  }

  @Transactional
  public Departamento buscarDepartamento(String id) throws ErrorServicio {
    try{
      Optional<Departamento> opt = repositorioDepartamento.findById(id);
      if (opt.isPresent()) {
        return opt.get();
      } else {
        throw new ErrorServicio("No se encontro el departamento solicitado");
      }
    }catch (Exception e){
      throw new ErrorServicio("Error del sistema");
    }
  }

  @Transactional
  public Departamento buscarDepartamentoPorNombre(String nombre) throws ErrorServicio {
    try{
      Departamento departamento = repositorioDepartamento.findByName(nombre);
      if (departamento != null) {
        return departamento;
      } else {
        throw new ErrorServicio("No se encontro el departamento solicitado");
      }
    }catch (Exception e){
      throw new ErrorServicio("Error del sistema");
    }
  }

  @Transactional
  public List<Departamento> buscarDepartamentoPorProvincia(String idProvincia) throws ErrorServicio {
    try{
      Provincia provincia = provinciaServicio.buscarProvincia(idProvincia);
      if (provincia == null) {
        throw new ErrorServicio("No se encontro la provincia solicitada");
      }
      List<Departamento> departamentos = repositorioDepartamento.findByProvincia(idProvincia);
      if (departamentos.isEmpty()) {
        throw new ErrorServicio("No hay departamentos cargados para la provincia solicitada");
      }
      departamentos.forEach(p -> p.getProvincia().getNombre());
      return departamentos;
    }catch (Exception e){
      throw new ErrorServicio("Error del sistema");
    }
  }

  // Escritura

  @Transactional
  public void crearDepartamento(String nombre, String idProvincia) throws ErrorServicio {
    validar(nombre, idProvincia);
    try{
      Provincia provincia = provinciaServicio.buscarProvincia(idProvincia);
      if (provincia == null) {
        throw new ErrorServicio("No se encontro la provincia solicitada");
      }
      Departamento departamento = new Departamento();
      departamento.setNombre(nombre);
      departamento.setProvincia(provincia);
      departamento.setEliminado(false);
      repositorioDepartamento.save(departamento);
    }catch (Exception e){
      throw new ErrorServicio("Error del sistema");
    }
  }

  @Transactional
  public void modificarDepartamento(String id, String nombre, String idProvincia) throws ErrorServicio {
    validar(nombre, idProvincia);
    try{
      if (id == null) {
        throw new ErrorServicio("El id del departamento no puede ser nulo");
      }
      Departamento departamento = buscarDepartamento(id);
      if (departamento == null) {
        throw new ErrorServicio("No se encontro el departamento solicitado");
      }
      Provincia provincia = provinciaServicio.buscarProvincia(idProvincia);
      if (provincia == null) {
        throw new ErrorServicio("No se encontro la provincia solicitada");
      }
      departamento.setNombre(nombre);
      departamento.setProvincia(provincia);
      repositorioDepartamento.save(departamento);
    }catch (Exception e){
      throw new ErrorServicio("Error del sistema");
    }
  }

  // Eliminacion

  @Transactional
  public void eliminarDepartamento(String id) throws ErrorServicio {
    try{
      if (id == null) {
        throw new ErrorServicio("El id del departamento no puede ser nulo");
      }
      Optional<Departamento> opt = repositorioDepartamento.findById(id);
      if (opt.isPresent()) {
        Departamento departamento = opt.get();
        departamento.setEliminado(true);
        repositorioDepartamento.save(departamento);
      } else {
        throw new ErrorServicio("No se encontro el departamento solicitado");
      }
    }catch (Exception e){
      throw new ErrorServicio("Error del sistema");
    }
  }

  // Validacion

  private void validar(String nombre, String idProvincia) throws ErrorServicio {
    if (nombre == null || nombre.isEmpty()) {
      throw new ErrorServicio("El nombre del departamento no puede ser nulo o estar vacio");
    }
    if (idProvincia == null || idProvincia.isEmpty()) {
      throw new ErrorServicio("El id de la provincia no puede ser nulo o estar vacio");
    }
  }

}
