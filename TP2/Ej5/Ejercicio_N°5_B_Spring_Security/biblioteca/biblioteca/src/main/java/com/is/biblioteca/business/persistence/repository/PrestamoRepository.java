package com.is.biblioteca.business.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.is.biblioteca.business.domain.entity.Prestamo;

@Repository
public interface PrestamoRepository extends JpaRepository<Prestamo, String> {

  List<Prestamo> findAllByEliminadoFalse();

  @Query("SELECT p FROM Prestamo p WHERE p.usuario.id = :idUsuario AND p.eliminado = false")
  List<Prestamo> findByUsuarioIdAndEliminadoFalse(@Param("idUsuario") String idUsuario);

}
