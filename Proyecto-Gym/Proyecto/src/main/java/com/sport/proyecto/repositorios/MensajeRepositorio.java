package com.sport.proyecto.repositorios;


import com.sport.proyecto.entidades.Mensaje;
import com.sport.proyecto.entidades.Pais;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MensajeRepositorio extends JpaRepository<Mensaje, Long>  {
    @Query(value = "SELECT * FROM mensaje m WHERE m.eliminado = false", nativeQuery = true)
    List<Mensaje> buscarTodosActivos();
}
