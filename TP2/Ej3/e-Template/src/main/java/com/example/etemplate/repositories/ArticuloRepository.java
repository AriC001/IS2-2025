package com.example.etemplate.repositories;

import com.example.etemplate.entities.Articulo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ArticuloRepository extends JpaRepository<Articulo, String> {
    @Query(value = "SELECT a FROM Articulo a WHERE a.deleted = false")
    List<Articulo> findAllActive();
}
