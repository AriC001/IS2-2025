package com.sport.proyecto.controladores;

import com.sport.proyecto.entidades.Empleado;
import com.sport.proyecto.entidades.Usuario;
import com.sport.proyecto.entidades.ValorCuota;
import com.sport.proyecto.servicios.*;
import jakarta.servlet.http.HttpSession;
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
    public String mostrarValorCuota(HttpSession session, Model model) {
        Usuario user = (Usuario) session.getAttribute("usuariosession");
        model.addAttribute("usuariosession", user);
        ValorCuota valorCuotaActual = valorCuotaServicio.obtenerValorActual();
        List<ValorCuota> valoresCuotaAnterioires = valorCuotaServicio.obtenerValorNoActual();
        model.addAttribute("valorCuotaActual",valorCuotaActual);
        model.addAttribute("valoresAnteriores",valoresCuotaAnterioires);
        return "views/valores-cuota";
    }

    @GetMapping("/nuevo")
    public String generarNuevoValorCuota(HttpSession session, Model model) {
        model.addAttribute("valorCuota", new ValorCuota());
        Usuario user = (Usuario) session.getAttribute("usuariosession");
        model.addAttribute("usuariosession", user);
        return "views/alta-valor-cuota";
        //ValorCuota valorCuotaActual = valorCuotaServicio.obtenerValorActual();
        //ValorCuota valorCuota = valorCuotaServicio.nuevoValorCuota()
    }

    @PostMapping("/nuevo")
    public String generarNuevoValorCuota(@ModelAttribute ValorCuota valorCuota,
                                         HttpSession session, Model model) {
        valorCuotaServicio.nuevoValorCuota(valorCuota.getValor(),valorCuota.getFechaHasta());
        Usuario user = (Usuario) session.getAttribute("usuariosession");
        model.addAttribute("usuariosession", user);
        return "redirect:/valor-cuota";
    }

}
