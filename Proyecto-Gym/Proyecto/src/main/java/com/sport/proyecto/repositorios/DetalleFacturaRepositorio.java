package com.sport.proyecto.repositorios;

import com.sport.proyecto.entidades.DetalleFactura;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface DetalleFacturaRepositorio extends JpaRepository<DetalleFactura,String> {
    @Query("SELECT df FROM DetalleFactura df WHERE df.factura.id = :facturaId")
    public List<DetalleFactura> findByFacturaId(@Param("facturaId") String facturaId);
    @Query("SELECT df FROM DetalleFactura df WHERE df.cuotaMensual.id = :cuotaId")
    public Optional<DetalleFactura> findByCuotaId(@Param("cuotaId") String cuotaId);
}
