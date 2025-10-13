package com.example.etemplate.servicios;

import com.example.etemplate.entities.CarritoTemplate;
import com.example.etemplate.repositories.CarritoRepository;
import com.example.etemplate.entities.CarritoTemplate;
import org.springframework.stereotype.Service;

@Service
public class CarritoService extends GenericServiceImpl<CarritoTemplate, String> {

    private final CarritoRepository carritoRepository;

    public CarritoService(CarritoRepository repository, CarritoRepository carritoRepository) {
        super(repository);
        this.carritoRepository = carritoRepository;
    }
}