package com.sport.proyecto.repositorios;

import com.sport.proyecto.entidades.Socio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SocioRepositorio extends JpaRepository<Socio,Long> {
    @Query("SELECT COALESCE(MAX(s.numeroSocio), 0) FROM Socio s")
    Long obtenerUltimoNumeroSocio();

    @Query(value = "SELECT * FROM socio WHERE socio.eliminado = false", nativeQuery = true)
    List<Socio> findAllActives();

    @Query(value = "SELECT * FROM socio WHERE socio.tipo_documento =:tipoDocumento AND socio.numero_documento =:numeroDocumento AND socio.eliminado = false", nativeQuery = true)
    Socio findByNumeroDocumentoYTipo(@Param("numeroDocumento") String numeroDocumento, @Param("tipoDocumento") String tipoDocumento);
}

//MAX(s.numeroSocio) devuelve el mayor numeroSocio actual.
//COALESCE(..., 0) asegura que si no hay registros, devuelva 0.

