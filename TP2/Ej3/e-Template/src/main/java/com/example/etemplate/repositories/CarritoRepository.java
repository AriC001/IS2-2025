package com.example.etemplate.repositories;

import com.example.etemplate.entities.CarritoBack;
import com.example.etemplate.entities.CarritoTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CarritoRepository extends JpaRepository<CarritoBack, String> {
    @Query("SELECT c FROM CarritoBack c WHERE c.usuario.id = :idUsuario and c.deleted = false")
    public List<CarritoBack> findByUsuarioID(String idUsuario);
}
