package com.example.etemplate.servicios;


import com.example.etemplate.entities.Detalle;
import com.example.etemplate.repositories.ArticuloRepository;
import com.example.etemplate.repositories.DetalleRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DetalleService extends GenericServiceImpl<Detalle, String> {

    private final DetalleRepository detalleRepository;

    public DetalleService(DetalleRepository repository, DetalleRepository detalleRepository) {
        super(repository);
        this.detalleRepository = detalleRepository;
    }
}

