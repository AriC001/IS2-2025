package com.sport.proyecto.repositorios;

import com.sport.proyecto.entidades.Pais;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaisRepositorio extends JpaRepository<Pais, Long> {

  @Query(value = "SELECT * FROM pais WHERE pais.eliminado = false", nativeQuery = true)
  List<Pais> buscarTodosActivos();

  @Query(value = "SELECT * FROM pais WHERE pais.id =: id AND pais.alta = true", nativeQuery = true)
  Pais buscarPorIdActivo(@Param("id") Long id);

  @Query(value = "SELECT * FROM pais WHERE pais.nombre = :nombre", nativeQuery = true)
  Pais buscarPorNombre(@Param("nombre") String nombre);



}
