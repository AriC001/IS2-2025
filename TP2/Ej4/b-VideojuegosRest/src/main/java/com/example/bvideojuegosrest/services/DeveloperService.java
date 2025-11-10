package com.example.bvideojuegosrest.services;

import com.example.bvideojuegosrest.entities.Developer;
import com.example.bvideojuegosrest.repositories.DeveloperRepository;
import org.springframework.stereotype.Service;

@Service
public class DeveloperService extends BaseService<Developer, Long> {

    public DeveloperService(DeveloperRepository repo) {
        super(repo);
    }

    @Override
    protected void validateEntity(Developer entity) throws Exception {
        if (entity == null) throw new Exception("Developer es nulo");
        if (entity.getName() == null || entity.getName().isBlank()) {
            throw new Exception("El nombre del developer es obligatorio");
        }
    }

    @Override
    protected Developer mergeForUpdate(Developer current, Developer incoming) {
        if (incoming.getName() != null && !incoming.getName().isBlank()) current.setName(incoming.getName());
        // no tocamos videogames desde aquí
        return current;
    }
}
