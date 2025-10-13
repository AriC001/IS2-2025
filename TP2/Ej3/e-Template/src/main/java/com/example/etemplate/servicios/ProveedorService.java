package com.example.etemplate.servicios;


import com.example.etemplate.entities.Proveedor;
import com.example.etemplate.repositories.ProveedorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ProveedorService {

    private final ProveedorRepository repo;

    public ProveedorService(ProveedorRepository repo) {
        this.repo = repo;
    }

    @Transactional
    public List<Proveedor> listar() {
        return repo.findAll();
    }
    @Transactional
    public Optional<Proveedor> buscarPorId(String id) {
        return repo.findById(id);
    }
    @Transactional
    public Proveedor guardar(Proveedor proveedor) {
        return repo.save(proveedor);
    }
    @Transactional
    public void eliminar(String id) {
        repo.deleteById(id);
    }
}