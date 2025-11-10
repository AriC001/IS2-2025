package com.sport.proyecto.repositorios;

import com.sport.proyecto.entidades.Factura;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FacturaRepositorio extends JpaRepository<Factura,String> {
    @Query("SELECT COALESCE(MAX(f.numeroFactura), 0) FROM Factura f")
    Long obtenerUltimoNumeroFactura();

    @Query("SELECT f FROM Factura f JOIN f.detalles d JOIN d.cuotaMensual c WHERE EXISTS (SELECT 1 FROM Socio s JOIN s.cuotas cm WHERE s.id = :socioId AND cm.id = c.id)")
    List<Factura> findFacturasBySocioId(@Param("socioId") String socioId);
    }

