package com.sport.proyecto.servicios;

import com.sport.proyecto.entidades.Socio;
import com.sport.proyecto.repositorios.SocioRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SocioServicio {
    @Autowired
    private SocioRepositorio repositorio;

    @Transactional
    public Long generarNumeroSocio() {
        Long ultimo = repositorio.obtenerUltimoNumeroSocio();
        return ultimo + 1;
    }

    @Transactional
    public Socio crearSocio(Socio socio) {
        socio.setNumeroSocio(generarNumeroSocio());
        return repositorio.save(socio);
    }
}
