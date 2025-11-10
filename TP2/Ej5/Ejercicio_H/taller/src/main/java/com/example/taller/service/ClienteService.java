package com.example.taller.service;

import com.example.taller.entity.Cliente;
import com.example.taller.error.ErrorServicio;
import com.example.taller.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ClienteService extends BaseService<Cliente, String> {

    private final ClienteRepository clienteRepository;

    public ClienteService(ClienteRepository repository) {
        super(repository);
        this.clienteRepository = repository;
    }

    @Override
    protected Cliente createEmpty() {
        Cliente c = new Cliente();
        c.setEliminado(false); // default coherente
        return c;
    }
    public Optional<Cliente>findById(String id){
        return clienteRepository.findById(id);
    }

    @Override
    protected void validar(Cliente entidad) throws ErrorServicio {
        if (entidad == null) throw new ErrorServicio("Cliente nulo");
        if (entidad.getDocumento() == null || entidad.getDocumento().isBlank())
            throw new ErrorServicio("El documento no puede ser nulo");
        if(entidad.getDocumento().length()<7 || entidad.getDocumento().length()>8){
            throw new ErrorServicio("El número de documento debe tener 7 u 8 dígitos");
        }
    }
}