package com.sport.proyecto.servicios;

import com.sport.proyecto.entidades.Direccion;
import com.sport.proyecto.entidades.Localidad;
import com.sport.proyecto.errores.ErrorServicio;
import com.sport.proyecto.repositorios.DireccionRepositorio;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DireccionServicio {

  @Autowired
  private DireccionRepositorio direccionRepositorio;

  @Autowired
  private LocalidadServicio localidadServicio;

  // Busqueda

  @Transactional
  public List<Direccion> listarDireccion() throws ErrorServicio{
    try {
      List<Direccion> direcciones = direccionRepositorio.findAll();
      if (direcciones.isEmpty()) {
        throw new ErrorServicio("No hay direcciones cargadas");
      }
      return direcciones;
    } catch (Exception e) {
      e.printStackTrace();
      throw new ErrorServicio("Error del sistema: " + e.getMessage());
    }
  }

  @Transactional
  public List<Direccion> listarDireccionActiva() throws ErrorServicio {
    try {
      List<Direccion> direcciones = direccionRepositorio.findAllActives();
      if (direcciones.isEmpty()) {
        throw new ErrorServicio("No hay direcciones cargadas");
      }
      return direcciones;
    } catch (Exception e) {
      e.printStackTrace();
      throw new ErrorServicio("Error del sistema: " + e.getMessage());
    }
  }
  @Transactional
  public Direccion buscarDireccion(Long id) throws ErrorServicio {
    try {
      Optional<Direccion> opt = direccionRepositorio.findById(id);
      if (opt.isPresent()) {
        return opt.get();
      } else {
        throw new ErrorServicio("No se encontro la direccion solicitada");
      }
    } catch (Exception e) {
      e.printStackTrace();
      throw new ErrorServicio("Error del sistema: " + e.getMessage());
    }
  }

  @Transactional
  public Direccion buscarDireccionPorCalleYNumeracion(String calle, String numeracion) throws ErrorServicio {
    try {
      Direccion direccion = direccionRepositorio.findByStreetAndNumber(calle, numeracion);
      if (direccion != null) {
        return direccion;
      } else {
        throw new ErrorServicio("No se encontro la direccion solicitada");
      }
    } catch (Exception e) {
      e.printStackTrace();
      throw new ErrorServicio("Error del sistema: " + e.getMessage());
    }
  }

  // Escritura

  @Transactional
  public void crearDireccion(String calle, String numeracion, String barrio, String manzanaPiso, String casaDepartamento, String referencia, Long idLocalidad) throws ErrorServicio {
    validar(calle, numeracion, barrio, manzanaPiso, casaDepartamento, referencia, idLocalidad);
    try {
      Localidad localidad = localidadServicio.buscarLocalidad(idLocalidad);
      if (localidad == null) {
        throw new ErrorServicio("No se encontro la localidad solicitada");
      }
      Direccion direccion = new Direccion();
      direccion.setCalle(calle);
      direccion.setNumeracion(numeracion);
      direccion.setBarrio(barrio);
      direccion.setManzanaPiso(manzanaPiso);
      direccion.setCasaDepartamento(casaDepartamento);
      direccion.setReferencia(referencia);
      direccion.setEliminado(false);
      direccion.setLocalidad(localidad);
      direccionRepositorio.save(direccion);

    } catch (Exception e) {
      e.printStackTrace();
      throw new ErrorServicio("Error del sistema: " + e.getMessage());
    }
  }

  @Transactional
  public void modificarDireccion(Long id, String calle, String numeracion, String barrio, String manzanaPiso, String casaDepartamento, String referencia, Long idLocalidad) throws ErrorServicio {
    validar(calle, numeracion, barrio, manzanaPiso, casaDepartamento, referencia, idLocalidad);
    try {
      Direccion direccion = buscarDireccion(id);
      if (direccion == null) {
        throw new ErrorServicio("No se encontro la direccion solicitada");
      }
      Localidad localidad = localidadServicio.buscarLocalidad(idLocalidad);
      if (localidad == null) {
        throw new ErrorServicio("No se encontro la localidad solicitada");
      }
      direccion.setCalle(calle);
      direccion.setNumeracion(numeracion);
      direccion.setBarrio(barrio);
      direccion.setManzanaPiso(manzanaPiso);
      direccion.setCasaDepartamento(casaDepartamento);
      direccion.setReferencia(referencia);
      direccion.setLocalidad(localidad);
      direccionRepositorio.save(direccion);

    } catch (Exception e) {
      e.printStackTrace();
      throw new ErrorServicio("Error del sistema: " + e.getMessage());
    }
  }

  @Transactional
  public void eliminarDireccion(Long id) throws ErrorServicio {
    try {
      Direccion direccion = buscarDireccion(id);
      if (direccion == null) {
        throw new ErrorServicio("No se encontro la direccion solicitada");
      }
      direccion.setEliminado(true);
      direccionRepositorio.save(direccion);
    } catch (Exception e) {
      e.printStackTrace();
      throw new ErrorServicio("Error del sistema: " + e.getMessage());
    }
  }

  // Validacion

  private void validar(String calle, String numeracion, String barrio, String manzanaPiso, String casaDepartamento, String referencia, Long idLocalidad) throws ErrorServicio {
    if (calle == null || calle.isEmpty()) {
      throw new ErrorServicio("La calle no puede ser nula o estar vacia");
    }
    if (numeracion == null || numeracion.isEmpty()) {
      throw new ErrorServicio("La numeracion no puede ser nula o estar vacia");
    }
    if (barrio == null || barrio.isEmpty()) {
      throw new ErrorServicio("El barrio no puede ser nulo o estar vacio");
    }
    if (manzanaPiso == null || manzanaPiso.isEmpty()) {
      throw new ErrorServicio("La manzana/piso no puede ser nulo o estar vacio");
    }
    if (casaDepartamento == null || casaDepartamento.isEmpty()) {
      throw new ErrorServicio("La casa/departamento no puede ser nulo o estar vacio");
    }
    if (idLocalidad == null || idLocalidad.toString().isEmpty()) {
      throw new ErrorServicio("La localidad no puede ser nula o estar vacia");
    }
  }

}
