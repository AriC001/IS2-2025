package com.sport.proyecto.repositorios;

import com.sport.proyecto.entidades.Empleado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface EmpleadoRepositorio  extends JpaRepository<Empleado,Long>  {

}
