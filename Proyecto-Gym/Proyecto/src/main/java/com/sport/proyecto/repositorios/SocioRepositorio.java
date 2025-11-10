package com.sport.proyecto.repositorios;

import com.sport.proyecto.entidades.Socio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SocioRepositorio extends JpaRepository<Socio,String> {
    @Query("SELECT COALESCE(MAX(s.numeroSocio), 0) FROM Socio s")
    Long obtenerUltimoNumeroSocio();

    @Query("SELECT s FROM Socio s WHERE s.numeroSocio = :numeroSocio and s.eliminado = false")
    Optional<Socio> findByNumeroSocio(Long numeroSocio);

    @Query("SELECT s.numeroSocio FROM Socio s WHERE s.usuario.id = :idUsuario and s.eliminado = false")
    Long findNroSocioByIdUsuario(@Param("idUsuario") String idUsuario);

    @Query("SELECT s FROM Socio s WHERE s.eliminado = false")
    public List<Socio> findAllActiveSocios();

    @Query(value = "SELECT * FROM socio WHERE socio.tipo_documento =:tipoDocumento AND socio.numero_documento =:numeroDocumento AND socio.eliminado = false", nativeQuery = true)
    Socio findByNumeroDocumentoYTipo(@Param("numeroDocumento") String numeroDocumento, @Param("tipoDocumento") String tipoDocumento);

}   

//MAX(s.numeroSocio) devuelve el mayor numeroSocio actual.
//COALESCE(..., 0) asegura que si no hay registros, devuelva 0.

