package com.sport.proyecto.repositorios;

import com.sport.proyecto.entidades.Departamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DepartamentoRepositorio extends JpaRepository<Departamento, Long> {
  
  @Query(value = "SELECT * FROM departamento WHERE departamento.eliminado = false", nativeQuery = true)
  List<Departamento> findAllActives();

  @Query(value = "SELECT * FROM departamento WHERE departamento.nombre = :nombre and departamento.eliminado = false", nativeQuery = true)
  Departamento findByName(@Param("nombre") String nombre);


}
