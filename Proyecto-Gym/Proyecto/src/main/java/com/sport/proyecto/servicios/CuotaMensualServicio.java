package com.sport.proyecto.servicios;

import com.sport.proyecto.entidades.CuotaMensual;
import com.sport.proyecto.entidades.Socio;
import com.sport.proyecto.entidades.ValorCuota;
import com.sport.proyecto.enums.estadoCuota;
import com.sport.proyecto.enums.mes;
import com.sport.proyecto.repositorios.CuotaMensualRepositorio;
import com.sport.proyecto.repositorios.SocioRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
             CuotaMensual cuota = new CuotaMensual();

             cuota.setMes(mes.values()[LocalDate.now().getMonthValue()-1]);
             cuota.setAnio((long) LocalDate.now().getYear());
             cuota.setFechaVencimiento(LocalDate.now().plusWeeks(2));
             cuota.setEliminado(false);
             cuota.setValorCuota(valor);
             cuota.setEstado(estadoCuota.ADEUDA);

             cuotaRepositorio.save(cuota);
             List<CuotaMensual> listaCuotas = new ArrayList<>();
             if(s.getCuotas() != null){
                 listaCuotas = s.getCuotas();
             }
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

    @Transactional
    public void actualizarCuota(CuotaMensual cuota) {
        cuotaRepositorio.save(cuota);
    }

    //List<CuotaMensual> cuotasNoPagadas = cuotaMensualServicio.obtenerCuotasNoPagadas(socio.getId());
    @Transactional
    public List<CuotaMensual> obtenerCuotasNoPagadas(Long numeroSocio){
        List<CuotaMensual> cuotas = socioServicio.buscarCuotasPendientes(numeroSocio);
        return cuotas;
    }

    public void generarCuotasMensuales(){
        List<Socio> socios = socioServicio.listarSocioActivos();
        for(Socio socio : socios){
            crearCuota(socio.getUsuario().getId());
        }
    }

    @Transactional
    public List<CuotaMensual> obtenerCuotasPagadas(Long numeroSocio) {
        List<CuotaMensual> cuotas = socioServicio.buscarCuotasPagadas(numeroSocio);
        return cuotas;
    }
}
