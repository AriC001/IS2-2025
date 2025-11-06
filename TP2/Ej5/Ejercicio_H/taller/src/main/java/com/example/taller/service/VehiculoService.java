package com.example.taller.service;
import org.springframework.stereotype.Service;
import com.example.taller.entity.*;
import com.example.taller.repository.*;
import com.example.taller.error.ErrorServicio;

@Service
public class VehiculoService extends BaseService<Vehiculo, String> {

    private final VehiculoRepository vehiculoRepository;

    public VehiculoService(VehiculoRepository repository) {
        super(repository);
        this.vehiculoRepository = repository;
    }

    @Override
    protected Vehiculo createEmpty() {
        Vehiculo v = new Vehiculo();
        v.setEliminado(false);
        return v;
    }

    @Override
    protected void validar(Vehiculo entidad) throws ErrorServicio {
        if (entidad == null) throw new ErrorServicio("Vehiculo nulo");
        if (entidad.getPatente() == null || entidad.getPatente().isBlank())
            throw new ErrorServicio("Patente es obligatoria");
    }
}