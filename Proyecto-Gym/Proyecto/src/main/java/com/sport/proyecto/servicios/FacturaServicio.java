package com.sport.proyecto.servicios;

import com.sport.proyecto.repositorios.FacturaRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FacturaServicio {
    @Autowired
    private FacturaRepositorio repositorioFactura;

    @Transactional
    public Long generarNumeroFactura() {
        Long ultimo = repositorioFactura.obtenerUltimoNumeroFactura();
        return ultimo + 1;
    }
}
