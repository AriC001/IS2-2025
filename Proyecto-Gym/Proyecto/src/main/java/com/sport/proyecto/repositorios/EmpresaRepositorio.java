package com.sport.proyecto.repositorios;

import com.sport.proyecto.entidades.Empresa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmpresaRepositorio extends JpaRepository<Empresa, String> {
  @Query(value = "SELECT * FROM empresa WHERE empresa.eliminado = false", nativeQuery = true)
  List<Empresa> findAllActives();

  @Query(value = "SELECT * FROM empresa WHERE empresa.nombre = :nombre and empresa.eliminado = false", nativeQuery = true)
  Empresa findByName(@Param("nombre") String nombre);

}
