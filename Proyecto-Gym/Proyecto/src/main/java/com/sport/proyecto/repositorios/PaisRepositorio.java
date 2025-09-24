package com.sport.proyecto.repositorios;

import com.sport.proyecto.entidades.Pais;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaisRepositorio extends JpaRepository<Pais, String> {

  @Query(value = "SELECT * FROM pais WHERE pais.eliminado = false", nativeQuery = true)
  List<Pais> findAllActives();

  @Query(value = "SELECT * FROM pais WHERE pais.nombre = :nombre and pais.eliminado = false", nativeQuery = true)
  Pais findByName(@Param("nombre") String nombre);



}
