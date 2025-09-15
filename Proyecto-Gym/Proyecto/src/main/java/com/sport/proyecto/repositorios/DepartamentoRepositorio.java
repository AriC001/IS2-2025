package com.sport.proyecto.repositorios;

import com.sport.proyecto.entidades.Departamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DepartamentoRepositorio extends JpaRepository<Departamento, Long> {
  
  @Query(value = "SELECT * FROM departamento WHERE departamento.alta = true", nativeQuery = true)
  List<Departamento> buscarTodosActivos();

  @Query(value = "SELECT * FROM departamento WHERE departamento.id =: id AND departamento.alta = true", nativeQuery = true)
  Departamento buscarPorIdActivo(@Param("id") Long id);

  @Query(value = "SELECT * FROM departamento WHERE departamento.nombre = :nombre", nativeQuery = true)
  Departamento buscarPorNombre(@Param("nombre") String nombre);


}
