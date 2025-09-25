package com.sport.proyecto.servicios;

import com.sport.proyecto.entidades.CuotaMensual;
import com.sport.proyecto.entidades.DetalleFactura;
import com.sport.proyecto.entidades.Factura;
import com.sport.proyecto.entidades.Socio;
import com.sport.proyecto.enums.estadoCuota;
import com.sport.proyecto.repositorios.CuotaMensualRepositorio;
import com.sport.proyecto.repositorios.FacturaRepositorio;
import com.sport.proyecto.repositorios.SocioRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.sport.proyecto.enums.estadoFactura;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class FacturaServicio {
    @Autowired
    private FacturaRepositorio facturaRepositorio;
    @Autowired
    private DetalleFacturaServicio detalleFacturaServicio;
    @Autowired
    private SocioServicio socioServicio;
    @Autowired
    private CuotaMensualServicio cuotaMensualServicio;
    @Autowired
    private CuotaMensualRepositorio cuotaMensualRepositorio;
    @Autowired
    private UsuarioServicio usuarioServicio;

    @Transactional
    public Long generarNumeroFactura() {
        Long ultimo = facturaRepositorio.obtenerUltimoNumeroFactura();
        return ultimo + 1;
    }

    @Transactional
    public Factura buscarFactura(String id){
        Factura factura = new Factura();
        Optional<Factura> opt = facturaRepositorio.findById(id);
        if(opt.isPresent()){
            factura = opt.get();
        }
        return factura;
    }

    @Transactional
    public void marcarComoPagada(String facturaId) {
        Factura factura = facturaRepositorio.findById(facturaId)
                .orElseThrow(() -> new RuntimeException("Factura no encontrada"));

        factura.setEstadoFactura(estadoFactura.PAGADO);

        for (DetalleFactura detalle : factura.getDetalles()) {
            CuotaMensual cuota = detalle.getCuotaMensual();
            cuota.setEstado(estadoCuota.PAGADA);
            cuotaMensualServicio.actualizarCuota(cuota);
        }

        facturaRepositorio.save(factura);
    }

    @Transactional
    public void marcarComoPendiente(String facturaId) {
        Factura factura = facturaRepositorio.findById(facturaId)
                .orElseThrow(() -> new RuntimeException("Factura no encontrada"));

        factura.setEstadoFactura(estadoFactura.PENDIENTE);

        for (DetalleFactura detalle : factura.getDetalles()) {
            CuotaMensual cuota = detalle.getCuotaMensual();
            cuota.setEstado(estadoCuota.PENDIENTE);
            cuotaMensualServicio.actualizarCuota(cuota);
        }

        facturaRepositorio.save(factura);
    }



    @Transactional
    public Factura generarFactura(String usuarioId, List<String> cuotasIds) {
        Socio socio = new Socio();
        Optional<Socio> opt = socioServicio.buscarSocioPorIdUsuario(usuarioId);
        if (opt.isPresent()){
            socio = opt.get();
        }

        Factura factura = new Factura();
        //factura.setId(UUID.randomUUID().toString());
        factura.setFechaFactura(LocalDate.now());
        factura.setEstadoFactura(estadoFactura.SIN_DEFINIR);
        factura.setEliminado(false);
        factura.setTotalPagado(0L);
        factura.setNumeroFactura(this.generarNumeroFactura());
        facturaRepositorio.save(factura);

        Long total = 0L;

        for (String cuotaId : cuotasIds) {
            CuotaMensual cuota = new CuotaMensual();
            Optional<CuotaMensual> opt2 = cuotaMensualRepositorio.findById(cuotaId);
            if(opt2.isPresent()){
                cuota = opt2.get();
                DetalleFactura detalle = detalleFacturaServicio.agregarDetalle(factura.getId(), cuotaId);
                total += detalle.getCuotaMensual().getValorCuota().getValor();
            }
        }

        factura.setTotalPagado(total);
        return facturaRepositorio.save(factura);

    }

    public List<Factura> obtenerFacturasDeSocio(String socioId) {
        return facturaRepositorio.findFacturasBySocioId(socioId);
    }

    public void pagarFactura(String facturaId) {
        Factura factura = new Factura();
        Optional<Factura> opt = facturaRepositorio.findById(facturaId);
        if (opt.isPresent()){
            factura = opt.get();
            factura.setEstadoFactura(estadoFactura.PAGADO);
            facturaRepositorio.save(factura);
        }
    }

    public void eliminarFactura(String facturaId) {
        Factura factura = new Factura();
        Optional<Factura> opt = facturaRepositorio.findById(facturaId);
        if (opt.isPresent()){
            factura = opt.get();
            factura.setEliminado(true);
            facturaRepositorio.save(factura);
        }
    }



}