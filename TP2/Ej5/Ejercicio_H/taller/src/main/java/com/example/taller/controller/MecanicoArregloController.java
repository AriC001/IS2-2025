package com.example.taller.controller;

import com.example.taller.dto.UsuarioSafeDTO;
import com.example.taller.entity.HistorialArreglo;
import com.example.taller.entity.Mecanico;
import com.example.taller.entity.Vehiculo;
import com.example.taller.error.ErrorServicio;
import com.example.taller.service.HistorialArregloService;
import com.example.taller.service.MecanicoService;
import com.example.taller.service.VehiculoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Controller
public class MecanicoArregloController {

    private final HistorialArregloService historialService;
    private final MecanicoService mecanicoService;
    private final VehiculoService vehiculoService;

    public MecanicoArregloController(HistorialArregloService historialService, MecanicoService mecanicoService, VehiculoService vehiculoService) {
        this.historialService = historialService;
        this.mecanicoService = mecanicoService;
        this.vehiculoService = vehiculoService;
    }

    @GetMapping("/mecanico/arreglos")
    public String listarPorMecanico(Model model, HttpSession session) {
        UsuarioSafeDTO usuario = (UsuarioSafeDTO) session.getAttribute("usuariosession");
        if (usuario == null) return "redirect:/login";

        // find mecanico by usuario id
        Mecanico mecanico = mecanicoService.findByUsuarioId(usuario.getId()).orElse(null);
        if (mecanico == null) {
            model.addAttribute("msgError", "No eres un mecánico");
            return "views/mis_arreglos_mecanico";
        }

        List<HistorialArreglo> lista = historialService.listarActivos().stream()
                .filter(h -> h.getMecanico() != null && mecanico.getId().equals(h.getMecanico().getId()))
                .toList();

        model.addAttribute("arreglos", lista);
        model.addAttribute("mecanico", mecanico);
        model.addAttribute("vehiculos", vehiculoService.listarActivos());
        return "views/mis_arreglos_mecanico";
    }

    @PostMapping("/mecanico/arreglos/add")
    public String addArreglo(@RequestParam String vehiculoId,
                             @RequestParam String detalle,
                             @RequestParam String fechaArreglo,
                             HttpSession session,
                             Model model) {
        UsuarioSafeDTO usuario = (UsuarioSafeDTO) session.getAttribute("usuariosession");
        if (usuario == null) return "redirect:/login";

        try {
            Mecanico mecanico = mecanicoService.findByUsuarioId(usuario.getId()).orElseThrow(() -> new ErrorServicio("No eres un mecánico"));
            Vehiculo v = vehiculoService.obtener(vehiculoId).orElseThrow(() -> new ErrorServicio("Vehículo no encontrado"));

            HistorialArreglo h = new HistorialArreglo();
            SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
            Date fecha = f.parse(fechaArreglo);
            h.setFecha(fecha);
            h.setDetalle(detalle);
            h.setMecanico(mecanico);
            h.setVehiculo(v);
            h.setEliminado(false);

            historialService.alta(h);
            model.addAttribute("msgExito", "Arreglo registrado correctamente");
        } catch (Exception e) {
            model.addAttribute("msgError", e.getMessage());
        }

        return "redirect:/mecanico/arreglos";
    }
}
