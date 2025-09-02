package edu.egg.tinder.servicios;

import edu.egg.tinder.entidades.Zona;
import edu.egg.tinder.errores.ErrorServicio;
import edu.egg.tinder.repositorios.ZonaRepositorio;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ZonaServicio {
    @Autowired
    private ZonaRepositorio zonaRepositorio;

    @Transactional
    public List<Zona> mostrarZonas() throws ErrorServicio {
        try{
        List<Zona> zonas = zonaRepositorio.findAll();
        return zonas;
        } catch (Exception e){
            throw new ErrorServicio("No se encontraron Zonas");
        }

    }
}
