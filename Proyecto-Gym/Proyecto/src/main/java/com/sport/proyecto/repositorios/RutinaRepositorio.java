package com.sport.proyecto.repositorios;

import com.sport.proyecto.entidades.Rutina;
import com.sport.proyecto.entidades.Socio;
import com.sport.proyecto.enums.EstadoRutina;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
@Repository
public interface RutinaRepositorio extends JpaRepository<Rutina, String> {
    @Query("SELECT r FROM Rutina r WHERE r.estado = 'EN_PROCESO'")
    Rutina findActiveRutina();
    @Query("SELECT r FROM Rutina r WHERE r.estado = 'FINALIZADA'")
    Rutina findFinalizedRutina();
    @Query("SELECT r FROM Rutina r WHERE r.estado = 'ANULADA'")
    Rutina findCancelledRutina();
    List<Rutina> findBySocio(Socio socio);
}
