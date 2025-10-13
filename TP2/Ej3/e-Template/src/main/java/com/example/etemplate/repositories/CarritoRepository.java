package com.example.etemplate.repositories;

import com.example.etemplate.entities.CarritoTemplate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CarritoRepository extends JpaRepository<CarritoTemplate, String> {
}
