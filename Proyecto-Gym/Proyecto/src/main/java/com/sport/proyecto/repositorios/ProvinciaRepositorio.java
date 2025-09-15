package com.sport.proyecto.repositorios;

import com.sport.proyecto.entidades.Provincia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProvinciaRepositorio extends JpaRepository<Provincia, Long> {
  
  @Query(value = "SELECT * FROM provincia WHERE provincia.alta = true", nativeQuery = true)
  List<Provincia> buscarTodosActivos();

  @Query(value = "SELECT * FROM provincia WHERE provincia.id =: id AND provincia.alta = true", nativeQuery = true)
  Provincia buscarPorIdActivo(@Param("id") Long id);

  @Query(value = "SELECT * FROM provincia WHERE provincia.nombre = :nombre", nativeQuery = true)
  Provincia buscarPorNombre(@Param("nombre") String nombre);

}
