package com.sport.proyecto.controladores;

import com.sport.proyecto.entidades.CuotaMensual;
import com.sport.proyecto.entidades.Factura;
import com.sport.proyecto.entidades.Socio;
import com.sport.proyecto.servicios.CuotaMensualServicio;
import com.sport.proyecto.servicios.FacturaServicio;
import com.sport.proyecto.servicios.PagoServicio;
import com.sport.proyecto.servicios.SocioServicio;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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

    @GetMapping
    public String mostrarCuotas(Model model, HttpSession session) {
        String username = (String) session.getAttribute("usuario");
        Socio socio = socioServicio.buscarSocioPorIdUsuario(username)
                .orElseThrow(() -> new RuntimeException("Socio no encontrado"));

        List<CuotaMensual> cuotasNoPagadas = cuotaMensualServicio.obtenerCuotasNoPagadas(socio.getNumeroSocio());
        model.addAttribute("cuotas", cuotasNoPagadas);

        return "cuotas-no-pagadas"; // Thymeleaf HTML
    }

    @PostMapping("/generar-factura")
    public String generarFactura(@RequestParam List<String> cuotasSeleccionadas,
                                 HttpSession session) {
        String username = (String) session.getAttribute("usuario");
        // Genera factura + detalles
        Factura factura = facturaServicio.generarFactura(username, cuotasSeleccionadas);

        // Redirige al endpoint de creación de pago con el facturaId
        return "redirect:/payment/create?facturaId=" + factura.getId();
    }
}
