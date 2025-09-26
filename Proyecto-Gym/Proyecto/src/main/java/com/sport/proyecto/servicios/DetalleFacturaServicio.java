package com.sport.proyecto.servicios;

import com.sport.proyecto.entidades.CuotaMensual;
import com.sport.proyecto.entidades.DetalleFactura;
import com.sport.proyecto.entidades.Factura;
import com.sport.proyecto.repositorios.DetalleFacturaRepositorio;
import com.sport.proyecto.repositorios.FacturaRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class DetalleFacturaServicio {
    @Autowired
    private CuotaMensualServicio cuotaMensualServicio;
    @Autowired
    private DetalleFacturaRepositorio detalleFacturaRepositorio;
    @Autowired
    private FacturaRepositorio facturaRepositorio;

    // Crear un detalle y asociarlo a una factura
    @Transactional
    public DetalleFactura agregarDetalle(String facturaId, String cuotaId) {
        if (detalleFacturaRepositorio.existsByFacturaIdAndCuotaMensualId(facturaId, cuotaId)) {
            throw new IllegalStateException("El detalle ya existe para esta cuota en la factura");
        }
        Factura factura = new Factura();
        Optional<Factura> opt = facturaRepositorio.findById(facturaId);
        if(opt.isPresent()){
            factura = opt.get();
        }
        CuotaMensual cuota = cuotaMensualServicio.buscarCuota(cuotaId);
        DetalleFactura detalle = new DetalleFactura();
        detalle.setFactura(factura);
        detalle.setCuotaMensual(cuota);
        detalle.setEliminado(false);

        // persistimos
        return detalleFacturaRepositorio.save(detalle);
    }

    public boolean existeDetalle(String facturaId, String cuotaId) {
        return detalleFacturaRepositorio.existsByFacturaIdAndCuotaMensualId(facturaId, cuotaId);
    }

    // Obtener todos los detalles de una factura
    @Transactional
    public List<DetalleFactura> obtenerDetallesPorFactura(String facturaId) {
       return detalleFacturaRepositorio.findByFacturaId(facturaId);
    }

    // Marcar un detalle como eliminado (soft delete)
    @Transactional
    public void eliminarDetalle(String detalleId) {
        DetalleFactura detalle = new DetalleFactura();
        Optional<DetalleFactura> opt = detalleFacturaRepositorio.findById(detalleId);
        if(opt.isPresent()){
            detalle = opt.get();
            detalle.setEliminado(true);
            detalleFacturaRepositorio.save(detalle);
        }
    }

    @Transactional
    public DetalleFactura buscarDetallePorCuota(String cuotaId){
        DetalleFactura detalle = new DetalleFactura();
        Optional<DetalleFactura> opt = detalleFacturaRepositorio.findByCuotaId(cuotaId);
        if(opt.isPresent()) {
            detalle = opt.get();
        }
        return detalle;
    }

}

