package com.example.etemplate.servicios;

import com.example.etemplate.entities.SoftDeletable;
import com.example.etemplate.repositories.ImagenRepository;
import com.example.etemplate.repositories.UserRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


public abstract class GenericServiceImpl<T, ID> implements GenericService<T, ID> {

    protected final JpaRepository<T, ID> repository;

    // 👇 Inyección por constructor
    protected GenericServiceImpl(JpaRepository<T, ID> repository) {
        this.repository = repository;
    }

    @Override
    public List<T> findAll() {
        return repository.findAll();
    }

    @Override
    public Optional<T> findById(ID id) {
        return repository.findById(id);
    }

    @Transactional
    @Override
    public T save(T entity) {
        return repository.save(entity);
    }

    @Override
    public void deleteById(ID id) {
        Optional<T> opt = repository.findById(id);
        if (opt.isPresent() && opt.get() instanceof SoftDeletable entity) {
            entity.setDeleted(true);
            repository.save(opt.get());
        } else {
            repository.deleteById(id); // fallback a delete físico si no implementa SoftDeletable
        }
    }


}
