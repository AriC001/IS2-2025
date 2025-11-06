package com.example.taller.service;


import org.springframework.stereotype.Service;

import com.example.taller.entity.HistorialArreglo;
import com.example.taller.error.ErrorServicio;
import com.example.taller.repository.HistorialArregloRepository;
import com.example.taller.repository.BaseRepository;
import java.util.*;

@Service
public class HistorialArregloService extends BaseService<HistorialArreglo, String> {

    public HistorialArregloService(BaseRepository<HistorialArreglo, String> repository) {
        super(repository);
    }

    @Override
    public HistorialArreglo createEmpty() {
        HistorialArreglo h = new HistorialArreglo();
        h.setEliminado(false);
        return h;
    }

    @Override
    public void validar(HistorialArreglo entidad) throws ErrorServicio {
        if (entidad == null) throw new ErrorServicio("Historial nulo");
        if (entidad.getDetalle() == null || entidad.getDetalle().isBlank())
            throw new ErrorServicio("Detalle del arreglo es obligatorio");
    }

    public Optional<HistorialArreglo> findById(String id){
        return repository.findById(id);
    }

}