package com.sport.proyecto.repositorios;

import com.sport.proyecto.entidades.Sucursal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SucursalRepositorio extends JpaRepository<Sucursal, Long> {

  @Query(value = "SELECT * FROM sucursal WHERE sucursal.alta = true", nativeQuery = true)
  List<Sucursal> buscarTodosActivos();

  @Query(value = "SELECT * FROM sucursal WHERE sucursal.id =: id AND sucursal.alta = true", nativeQuery = true)
  Sucursal buscarPorIdActivo(@Param("id") Long id);

  @Query(value = "SELECT * FROM sucursal WHERE sucursal.nombre = :nombre", nativeQuery = true)
  Sucursal buscarPorNombre(@Param("nombre") String nombre);

}
