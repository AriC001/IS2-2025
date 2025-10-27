package com.is.biblioteca.business.logic.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.is.biblioteca.business.domain.entity.Libro;
import com.is.biblioteca.business.domain.entity.Prestamo;
import com.is.biblioteca.business.domain.entity.Usuario;
import com.is.biblioteca.business.logic.error.ErrorServiceException;
import com.is.biblioteca.business.persistence.repository.PrestamoRepository;

import jakarta.transaction.Transactional;

@Service
public class PrestamoService {

  @Autowired
  private PrestamoRepository prestamoRepository;

  @Autowired
  private LibroService libroService;


  @Transactional
  public void crearPrestamo(Usuario usuario, Libro libro, Date fechaPrestamo, Date fechaDevolucion) throws ErrorServiceException {
    validar(usuario, libro, fechaPrestamo, fechaDevolucion);

    Prestamo prestamo = new Prestamo();
    prestamo.setId(java.util.UUID.randomUUID().toString());
    prestamo.setUsuario(usuario);

    libroService.actualizarStockLibro(libro.getId(), -1);
    prestamo.setLibro(libro);

    prestamo.setFechaPrestamo(fechaPrestamo);
    prestamo.setFechaDevolucion(fechaDevolucion);
    prestamoRepository.save(prestamo);
  }

  @Transactional
  public List<Prestamo> listarPrestamos() {
    return prestamoRepository.findAllByEliminadoFalse();
  }

  @Transactional
  public List<Prestamo> listarPrestamosPorUsuario(String idUsuario) {
    return prestamoRepository.findByUsuarioIdAndEliminadoFalse(idUsuario);
  }

  @Transactional
  public Prestamo buscarPrestamoPorId(String id) {
    return prestamoRepository.findById(id).orElse(null);
  }

  @Transactional
  public Prestamo actualizarPrestamo(String id, Usuario usuario, Libro libro, Date fechaPrestamo, Date fechaDevolucion) {
    validar(usuario, libro, fechaPrestamo, fechaDevolucion);

    Prestamo prestamo = buscarPrestamoPorId(id);
    if (prestamo == null) {
      throw new IllegalArgumentException("Préstamo no encontrado");
    }

    prestamo.setUsuario(usuario);
    prestamo.setLibro(libro);
    prestamo.setFechaPrestamo(fechaPrestamo);
    prestamo.setFechaDevolucion(fechaDevolucion);

    return prestamoRepository.save(prestamo);
  }

  @Transactional
  public void eliminarPrestamo(String id) throws ErrorServiceException {
    Prestamo prestamo = buscarPrestamoPorId(id);
    if (prestamo != null) {
      prestamo.setEliminado(true);
      libroService.actualizarStockLibro(prestamo.getLibro().getId(), +1);
      prestamoRepository.save(prestamo);
    }
  }

  void validar(Usuario usuario, Libro libro, Date fechaPrestamo, Date fechaDevolucion) {
    // Validaciones para crear un préstamo
    if (usuario == null) {
      throw new IllegalArgumentException("El usuario no puede ser nulo");
    }
    if (libro == null) {
      throw new IllegalArgumentException("El libro no puede ser nulo");
    }
    if (fechaPrestamo == null) {
      throw new IllegalArgumentException("La fecha de préstamo no puede ser nula");
    }
    if (fechaDevolucion == null) {
      throw new IllegalArgumentException("La fecha de devolución no puede ser nula");
    }
  }

}



