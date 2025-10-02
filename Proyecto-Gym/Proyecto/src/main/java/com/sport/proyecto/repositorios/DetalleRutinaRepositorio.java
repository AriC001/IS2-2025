package com.sport.proyecto.repositorios;

import java.util.Collection;
import com.sport.proyecto.entidades.Rutina;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.sport.proyecto.entidades.DetalleRutina;
@Repository

public interface DetalleRutinaRepositorio extends JpaRepository<DetalleRutina, String> {

    @Query("SELECT d FROM DetalleRutina d WHERE d.estado = 'SIN_REALIZAR' AND d.rutina.id = :rutinaId")
    Collection<DetalleRutina> findActividadesSinRealizar(@Param("rutinaId") String rutinaId);

    @Query("SELECT d FROM DetalleRutina d WHERE d.estado = 'REALIZADA' AND d.rutina.id = :rutinaId")
    Collection<DetalleRutina> findActividadesRealizadas(@Param("rutinaId") String rutinaId);
}

