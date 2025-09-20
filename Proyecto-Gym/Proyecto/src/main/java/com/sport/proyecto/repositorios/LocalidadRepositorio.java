package com.sport.proyecto.repositorios;

import com.sport.proyecto.entidades.Localidad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LocalidadRepositorio extends JpaRepository<Localidad, Long> {
  
  @Query(value = "SELECT * FROM localidad WHERE localidad.eliminado = false", nativeQuery = true)
  List<Localidad> findAllActives();

  @Query(value = "SELECT * FROM localidad WHERE localidad.codigo_postal = :codigoPostal and localidad.eliminado = false", nativeQuery = true)
  Localidad findByPostalCode(@Param("codigoPostal") String codigoPostal);
  
  @Query(value = "SELECT * FROM localidad WHERE localidad.nombre = :nombre and localidad.eliminado = false", nativeQuery = true)
  Localidad findByName(@Param("nombre") String nombre);

  @Query(value = "SELECT * FROM localidad WHERE localidad.id_departamento = :idDepartamento and localidad.eliminado = false", nativeQuery = true)
  List<Localidad> findByDepartamento(@Param("idDepartamento") Long idDepartamento);

}
