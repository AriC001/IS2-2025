package com.sport.proyecto.repositorios;

import com.sport.proyecto.entidades.CuotaMensual;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CuotaMensualRepositorio extends JpaRepository<CuotaMensual,String> {

}
