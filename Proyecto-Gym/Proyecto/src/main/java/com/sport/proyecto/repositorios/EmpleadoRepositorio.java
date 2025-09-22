package com.sport.proyecto.repositorios;

import com.sport.proyecto.entidades.Empleado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmpleadoRepositorio  extends JpaRepository<Empleado,String>  {
    @Query("SELECT e FROM Empleado e WHERE e.tipoEmpleado = 'PROFESOR' and e.eliminado = false and e.id=:id")
    Empleado findProfesor(@Param("id") String id);
    @Query("SELECT e FROM Empleado e WHERE e.usuario.id = :idUsuario and e.eliminado = false")
    Empleado findEmpleadoByIdUsuario(String idUsuario);
    @Query("SELECT e FROM Empleado e WHERE e.tipoEmpleado = 'PROFESOR' and e.eliminado = false")
    public List<Empleado> buscarEmpleadosActivos();
    @Query("SELECT e FROM Empleado e WHERE e.tipoEmpleado = 'PROFESOR' and e.eliminado = false")
    public List<Empleado> findAllProfesores();
  @Query("SELECT e FROM Empleado e WHERE e.numeroDocumento = :numeroDocumento and e.eliminado = false")
  public Optional<Empleado> findByNumeroDocumento(@Param("numeroDocumento") String numeroDocumento);

}   
