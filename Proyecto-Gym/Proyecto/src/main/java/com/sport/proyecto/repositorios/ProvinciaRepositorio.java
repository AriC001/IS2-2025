package com.sport.proyecto.repositorios;

import com.sport.proyecto.entidades.Provincia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProvinciaRepositorio extends JpaRepository<Provincia, String> {
  
  @Query(value = "SELECT * FROM provincia WHERE provincia.eliminado = false", nativeQuery = true)
  List<Provincia> findAllActives();

  @Query(value = "SELECT * FROM provincia WHERE provincia.nombre = :nombre and provincia.eliminado = false", nativeQuery = true)
  Provincia findByName(@Param("nombre") String nombre);

  @Query(value = "SELECT * FROM provincia WHERE provincia.pais_id = :idPais and provincia.eliminado = false", nativeQuery = true)
  List<Provincia> findByPais(@Param("idPais") String idPais);

}
