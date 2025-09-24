package com.sport.proyecto.servicios;

import com.sport.proyecto.entidades.CuotaMensual;
import com.sport.proyecto.entidades.Socio;
import com.sport.proyecto.entidades.Usuario;
import com.sport.proyecto.entidades.ValorCuota;
import com.sport.proyecto.enums.mes;
import com.sport.proyecto.repositorios.CuotaMensualRepositorio;
import com.sport.proyecto.repositorios.SocioRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Array;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CuotaMensualServicio {
    @Autowired
    private CuotaMensualRepositorio cuotaRepositorio;
    @Autowired
    private ValorCuotaServicio valorCuotaServicio;
    @Autowired
    private SocioServicio socioServicio;
    @Autowired
    private SocioRepositorio socioRepositorio;

     @Transactional
     public void crearCuota(String idUsuario){
         Optional<Socio> opt = socioServicio.buscarSocioPorIdUsuario(idUsuario);
         if(opt.isPresent()){
             Socio s = opt.get();
             ValorCuota valor = valorCuotaServicio.obtenerValorActual();
             CuotaMensual cuota = CuotaMensual.builder().
                     mes(mes.actual(LocalDate.now().getMonthValue())).
                     anio((long) LocalDate.now().getYear()).
                     fechaVencimiento(LocalDate.now().plusWeeks(2)).
                     eliminado(false).
                     valorCuota(valor).build();
             cuotaRepositorio.save(cuota);
             List<CuotaMensual> listaCuotas = s.getCuotas();
             listaCuotas.add(cuota);
             s.setCuotas(listaCuotas);
             socioRepositorio.save(s);
         }

     }
     @Transactional
     public CuotaMensual buscarCuota(String id){
        Optional<CuotaMensual> opt = cuotaRepositorio.findById(id);
        if(opt.isPresent()){
            return opt.get();
        }
        return null;
     }


}
