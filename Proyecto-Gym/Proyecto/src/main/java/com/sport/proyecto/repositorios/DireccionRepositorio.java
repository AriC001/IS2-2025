package com.sport.proyecto.repositorios;

import com.sport.proyecto.entidades.Direccion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DireccionRepositorio extends JpaRepository<Direccion, Long> {

}
