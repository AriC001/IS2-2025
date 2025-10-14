package com.example.etemplate.servicios;

import com.example.etemplate.entities.*;
import com.example.etemplate.repositories.CarritoRepository;
import com.example.etemplate.entities.CarritoTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CarritoService extends GenericServiceImpl<CarritoBack, String> {

    private final CarritoRepository carritoRepository;

    @Autowired
    public CarritoService(CarritoRepository repository, CarritoRepository carritoRepository) {
        super(repository);
        this.carritoRepository = carritoRepository;
    }

    public List<CarritoBack> findByUsuario(String idUsuario) {
        return carritoRepository.findByUsuarioID(idUsuario);
    }

}