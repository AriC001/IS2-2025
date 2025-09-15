package com.sport.proyecto.repositorios;

import com.sport.proyecto.entidades.Localidad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LocalidadRepositorio extends JpaRepository<Localidad, Long> {
  
  @Query(value = "SELECT * FROM localidad WHERE localidad.alta = true", nativeQuery = true)
  List<Localidad> buscarTodosActivos();

  @Query(value = "SELECT * FROM localidad WHERE localidad.id =: id AND localidad.alta = true", nativeQuery = true)
  Localidad buscarPorIdActivo(@Param("id") Long id);

  @Query(value = "SELECT * FROM localidad WHERE localidad.codigo_postal = :codigoPostal", nativeQuery = true)
  Localidad buscarPorCodigoPostal(@Param("codigoPostal") String codigoPostal);
  
  @Query(value = "SELECT * FROM localidad WHERE localidad.nombre = :nombre", nativeQuery = true)
  Localidad buscarPorNombre(@Param("nombre") String nombre);

}
