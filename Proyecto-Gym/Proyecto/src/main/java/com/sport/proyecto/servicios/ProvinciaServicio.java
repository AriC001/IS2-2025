package com.sport.proyecto.servicios;

import com.sport.proyecto.entidades.Departamento;
import com.sport.proyecto.entidades.Pais;
import com.sport.proyecto.entidades.Provincia;
import com.sport.proyecto.errores.ErrorServicio;
import com.sport.proyecto.repositorios.ProvinciaRepositorio;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ProvinciaServicio {
  @Autowired
  private ProvinciaRepositorio repositorioProvincia;

  @Autowired
  private PaisServicio paisServicio;

  // Busqueda

  @Transactional
  public List<Provincia> listarProvincia() throws ErrorServicio {
    try{
      List<Provincia> provincias = repositorioProvincia.findAll();
      if (provincias.isEmpty()) {
        throw new ErrorServicio("No hay provincias cargadas");
      }
      return repositorioProvincia.findAll();
    }catch (Exception e){
      throw new ErrorServicio("Error del sistema");
    }

  }

  @Transactional
  public List<Provincia> listarProvinciaActiva() throws ErrorServicio {
    try {
      List<Provincia> provincias = repositorioProvincia.findAllActives();
      if (provincias.isEmpty()) {
        throw new ErrorServicio("No hay provincias cargadas");
      }
      return repositorioProvincia.findAllActives();
    } catch (Exception e) {
      throw new ErrorServicio("Error del sistema");
    }
  }

  @Transactional
  public Provincia buscarProvincia(String id) throws ErrorServicio {
    try {
      Optional<Provincia> provincia = repositorioProvincia.findById(id);
      if (provincia.isPresent()) {
        return provincia.get();
      } else {
        throw new ErrorServicio("No se encontro la provincia solicitada");
      }
    } catch (Exception e) {
      throw new ErrorServicio("Error del sistema");
    }
  }

  @Transactional
  public Provincia buscarProvinciaPorNombre(String nombre) throws ErrorServicio {
    try {
      Provincia provincia = repositorioProvincia.findByName(nombre);
      if (provincia != null) {
        return provincia;
      } else {
        throw new ErrorServicio("No se encontro la provincia solicitada");
      }
    } catch (Exception e) {
      throw new ErrorServicio("Error del sistema");
    }
  }

  @Transactional
  public List<Provincia> buscarProvinciaPorPais(String id) throws ErrorServicio {
    try {
      List<Provincia> provincias = repositorioProvincia.findByPais(id);
      if (provincias != null && !provincias.isEmpty()) {
        return provincias;
      } else {
        throw new ErrorServicio("No se encontraron provincias para el pais solicitado");
      }
    } catch (Exception e) {
      throw new ErrorServicio("Error del sistema");
    }
  }

  // Escritura

  @Transactional
  public void crearProvincia(String nombre, String idPais) throws ErrorServicio {
    validar(nombre, idPais);
    try{
      Pais pais = paisServicio.buscarPais(idPais);
      if (pais == null) {
        throw new ErrorServicio("No se encontro el pais solicitado");
      }
      Provincia provincia = new Provincia();
      provincia.setId(UUID.randomUUID().toString());
      provincia.setNombre(nombre);
      provincia.setPais(pais);
      provincia.setEliminado(false);
      repositorioProvincia.save(provincia);
    }catch (Exception e){
      throw new ErrorServicio("Error del sistema");
    }
  }

  @Transactional
  public void modificarProvincia(String id, String nombre, String idPais) throws ErrorServicio {
    validar(nombre, idPais);
    try{
      if (id == null) {
        throw new ErrorServicio("El id de la provincia no puede ser nulo");
      }
      Provincia provincia = buscarProvincia(id);
      if (provincia == null) {
        throw new ErrorServicio("No se encontro la provincia solicitada");
      }
      Pais pais = paisServicio.buscarPais(idPais);
      if (pais == null) {
        throw new ErrorServicio("No se encontro el pais solicitado");
      }
      provincia.setNombre(nombre);
      provincia.setPais(pais);
      repositorioProvincia.save(provincia);
    }catch (Exception e){
      throw new ErrorServicio("Error del sistema");
    }
  }

  // Eliminacion

  @Transactional
  public void eliminarProvincia(String id) throws ErrorServicio {
    try{
      if (id == null) {
        throw new ErrorServicio("El id de la provincia no puede ser nulo");
      }
      Optional<Provincia> opt = repositorioProvincia.findById(id);
      if (opt.isPresent()) {
        Provincia provincia = opt.get();
        provincia.setEliminado(true);
        repositorioProvincia.save(provincia);
      } else {
        throw new ErrorServicio("No se encontro la provincia solicitada");
      }
    }catch (Exception e){
      throw new ErrorServicio("Error del sistema");
    }
  }

  // Validacion

  private void validar(String nombre, String idPais) throws ErrorServicio {
    if (nombre == null || nombre.isEmpty()) {
      throw new ErrorServicio("El nombre de la provincia no puede ser nulo o estar vacio");
    }
    if (idPais == null || idPais.isEmpty()) {
      throw new ErrorServicio("El id del pais no puede ser nulo o estar vacio");
    }

  }
}
