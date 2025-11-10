package com.sport.proyecto.controladores;

import com.sport.proyecto.entidades.Empleado;
import com.sport.proyecto.entidades.Usuario;
import com.sport.proyecto.entidades.ValorCuota;
import com.sport.proyecto.servicios.*;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/valor-cuota")
public class ValorCuotaControlador {

    @Autowired
    private CuotaMensualServicio cuotaMensualServicio;
    @Autowired
    private ValorCuotaServicio valorCuotaServicio;

    @GetMapping("")
    public String mostrarValorCuota(Model model, @ModelAttribute("usuariosession") Usuario usuariosession) {
        ValorCuota valorCuotaActual = valorCuotaServicio.obtenerValorActual();
        List<ValorCuota> valoresCuotaAnterioires = valorCuotaServicio.obtenerValorNoActual();
        model.addAttribute("valorCuotaActual",valorCuotaActual);
        model.addAttribute("valoresAnteriores",valoresCuotaAnterioires);
        return "views/valores-cuota";
    }

    @GetMapping("/nuevo")
    public String generarNuevoValorCuota(Model model, @ModelAttribute("usuariosession") Usuario usuariosession) {
        model.addAttribute("valorCuota", new ValorCuota());
        // usuariosession is available in the model via GlobalControllerAdvice
        return "views/alta-valor-cuota";
        //ValorCuota valorCuotaActual = valorCuotaServicio.obtenerValorActual();
        //ValorCuota valorCuota = valorCuotaServicio.nuevoValorCuota()
    }

    @PostMapping("/nuevo")
    public String generarNuevoValorCuota(@ModelAttribute ValorCuota valorCuota,
                                         @ModelAttribute("usuariosession") Usuario usuariosession, Model model) {
        valorCuotaServicio.nuevoValorCuota(valorCuota.getValor(),valorCuota.getFechaHasta());
        return "redirect:/valor-cuota";
    }

}
