package com.sport.proyecto.repositorios;

import com.sport.proyecto.entidades.Sucursal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SucursalRepositorio extends JpaRepository<Sucursal, String> {

  @Query(value = "SELECT * FROM sucursal WHERE sucursal.eliminado = false", nativeQuery = true)
  List<Sucursal> findAllActives();

  @Query(value = "SELECT * FROM sucursal WHERE sucursal.nombre = :nombre and sucursal.eliminado = false", nativeQuery = true)
  Sucursal findByName(@Param("nombre") String nombre);

}
