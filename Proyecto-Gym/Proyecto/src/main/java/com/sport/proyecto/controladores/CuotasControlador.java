package com.sport.proyecto.controladores;

import com.sport.proyecto.entidades.*;
import com.sport.proyecto.servicios.*;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/cuotas")
public class CuotasControlador {

    @Autowired
    private SocioServicio socioServicio;

    @Autowired
    private CuotaMensualServicio cuotaMensualServicio;

    @Autowired
    private FacturaServicio facturaServicio;

    @Autowired
    private PagoServicio pagoServicio;
    @Autowired
    private DetalleFacturaServicio detalleFacturaServicio;
    @Autowired
    private ValorCuotaServicio valorCuotaServicio;

    @GetMapping
    public String mostrarCuotas(Model model, @ModelAttribute("usuariosession") Usuario user) {
        Socio socio = socioServicio.buscarSocioPorIdUsuario(user.getId())
                .orElseThrow(() -> new RuntimeException("Socio no encontrado"));
        List<CuotaMensual> cuotasNoPagadas = cuotaMensualServicio.obtenerCuotasNoPagadas(socio.getNumeroSocio());
        List<CuotaMensual> cuotasPagadas = cuotaMensualServicio.obtenerCuotasPagadas(socio.getNumeroSocio());
        model.addAttribute("cuotas", cuotasNoPagadas);
        model.addAttribute("cuotasPagadas", cuotasPagadas);
        System.out.println(cuotasPagadas);
        return "views/cuotas-no-pagadas"; // Thymeleaf HTML
    }

    @PostMapping("/generar-factura")
    public String generarFactura(@RequestParam List<String> cuotasSeleccionadas,
                                 @ModelAttribute("usuariosession") Usuario user) {
        // Genera factura + detalles
        Factura factura = facturaServicio.generarFactura(user.getId(), cuotasSeleccionadas);

        // Redirige al endpoint de creación de pago con el facturaId
        return "redirect:/payment/create?facturaId=" + factura.getId();
    }
    /*
    @GetMapping("/valor-cuota")
    public String generarValorCuota(HttpSession session,Model model) {
        Usuario user = (Usuario) session.getAttribute("usuariosession");
        ValorCuota valorCuotaActual = valorCuotaServicio.obtenerValorActual();
        List<ValorCuota> valoresCuotaAnterioires = valorCuotaServicio.obtenerValorNoActual();
        model.addAttribute("valorCuotaActual",valorCuotaActual);
        model.addAttribute("valoresAnteriores",valoresCuotaAnterioires);
        return "views/valores-cuota";
    }
    */





}
