package com.sport.proyecto.servicios;

import com.sport.proyecto.entidades.ValorCuota;
import com.sport.proyecto.repositorios.ValorCuotaRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class ValorCuotaServicio {
    @Autowired
    private ValorCuotaRepositorio valorCuotaRepositorio;

    @Transactional
    public ValorCuota obtenerValorActual(){
        List<ValorCuota> valores = valorCuotaRepositorio.obtenerValorActualoUltimo(LocalDate.now());
        if(!valores.isEmpty()){
            ValorCuota v = valores.get(0);
            return v;
        }
        return null;
    }

    @Transactional
    public ValorCuota nuevoValorCuota(Long valor,LocalDate fechaHasta){
        List<ValorCuota> valores = valorCuotaRepositorio.obtenerValorActualoUltimo(LocalDate.now());
        if(!valores.isEmpty()){
            ValorCuota v_old = valores.get(0);
            v_old.setFechaHasta(LocalDate.now().minusDays(1));
        }
        ValorCuota v = ValorCuota.builder().
                fechaDesde(LocalDate.now()).
                fechaHasta(fechaHasta).
                valor(valor).
                eliminado(false).build();
        valorCuotaRepositorio.save(v);
        return v;
    }

    public List<ValorCuota> obtenerValorNoActual() {
        List<ValorCuota> valores = valorCuotaRepositorio.obtenerValoresAnteriores(LocalDate.now());
        return valores;
    }
}
