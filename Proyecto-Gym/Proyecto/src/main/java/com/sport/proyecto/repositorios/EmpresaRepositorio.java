package com.sport.proyecto.repositorios;

import com.sport.proyecto.entidades.Empresa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmpresaRepositorio extends JpaRepository<Empresa, Long> {
  @Query(value = "SELECT * FROM empresa WHERE empresa.alta = true", nativeQuery = true)
  List<Empresa> buscarTodosActivos();

  @Query(value = "SELECT * FROM empresa WHERE empresa.id =: id AND empresa.alta = true", nativeQuery = true)
  Empresa buscarPorIdActivo(@Param("id") Long id);

  @Query(value = "SELECT * FROM empresa WHERE empresa.nombre = :nombre", nativeQuery = true)
  Empresa buscarPorNombre(@Param("nombre") String nombre);

}
