package com.sport.proyecto.repositorios;

import com.sport.proyecto.entidades.Socio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface SocioRepositorio extends JpaRepository<Socio,Long> {
    @Query("SELECT COALESCE(MAX(s.numeroSocio), 0) FROM Socio s")
    Long obtenerUltimoNumeroSocio();
}

//MAX(s.numeroSocio) devuelve el mayor numeroSocio actual.
//COALESCE(..., 0) asegura que si no hay registros, devuelva 0.

