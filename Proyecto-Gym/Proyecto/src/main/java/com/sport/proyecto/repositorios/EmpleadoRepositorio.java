package com.sport.proyecto.repositorios;

import com.sport.proyecto.entidades.Empleado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmpleadoRepositorio  extends JpaRepository<Empleado,String>  {
  @Query(value = "SELECT * FROM empleado WHERE empleado.eliminado = false", nativeQuery = true)
  public List<Empleado> buscarEmpleadosActivos();

}
