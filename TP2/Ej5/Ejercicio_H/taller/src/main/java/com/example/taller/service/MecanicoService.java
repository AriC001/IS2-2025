package com.example.taller.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.taller.entity.Mecanico;
import com.example.taller.error.ErrorServicio;
import com.example.taller.repository.MecanicoRepository;


@Service
public class MecanicoService extends BaseService<Mecanico, String> {

    private final MecanicoRepository mecanicoRepository;

    public MecanicoService(MecanicoRepository repository) {
        super(repository);
        this.mecanicoRepository = repository;
    }

    @Override
    protected Mecanico createEmpty() {
        Mecanico m = new Mecanico();
        m.setEliminado(false);
        return m;
    }

    @Override
    protected void validar(Mecanico entidad) throws ErrorServicio {
        if (entidad == null) throw new ErrorServicio("Mecánico nulo");
        if (entidad.getLegajo() == null || entidad.getLegajo().isBlank())
            throw new ErrorServicio("Legajo es obligatorio");
    }
}