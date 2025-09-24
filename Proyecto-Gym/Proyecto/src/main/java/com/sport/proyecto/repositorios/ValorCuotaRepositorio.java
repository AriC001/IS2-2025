package com.sport.proyecto.repositorios;

import com.sport.proyecto.entidades.ValorCuota;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ValorCuotaRepositorio extends JpaRepository<ValorCuota,String> {
    @Query(value = "SELECT v FROM ValorCuota v WHERE v.id = '1'")
    Optional<ValorCuota> obtenerPrimerValorCuota();
}



