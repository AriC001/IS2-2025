package com.sport.proyecto.controladores;

import com.sport.proyecto.entidades.*;
import com.sport.proyecto.servicios.*;
import jakarta.servlet.http.HttpSession;
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

    @GetMapping
    public String mostrarCuotas(Model model, HttpSession session) {
        System.out.println(session);
        Usuario user = (Usuario) session.getAttribute("usuariosession");
        Socio socio = socioServicio.buscarSocioPorIdUsuario(user.getId())
                .orElseThrow(() -> new RuntimeException("Socio no encontrado"));
        // 👇 esto es lo que te faltaba
        model.addAttribute("usuariosession", user);
        List<CuotaMensual> cuotasNoPagadas = cuotaMensualServicio.obtenerCuotasNoPagadas(socio.getNumeroSocio());
        List<CuotaMensual> cuotasPagadas = cuotaMensualServicio.obtenerCuotasPagadas(socio.getNumeroSocio());
        model.addAttribute("cuotas", cuotasNoPagadas);
        model.addAttribute("cuotasPagadas", cuotasPagadas);
        System.out.println(cuotasPagadas);
        return "views/cuotas-no-pagadas"; // Thymeleaf HTML
    }

    @PostMapping("/generar-factura")
    public String generarFactura(@RequestParam List<String> cuotasSeleccionadas,
                                 HttpSession session) {
        Usuario user = (Usuario) session.getAttribute("usuariosession");
        // Genera factura + detalles
        Factura factura = facturaServicio.generarFactura(user.getId(), cuotasSeleccionadas);

        // Redirige al endpoint de creación de pago con el facturaId
        return "redirect:/payment/create?facturaId=" + factura.getId();
    }





}
