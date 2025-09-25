package com.sport.proyecto.controladores;

import com.sport.proyecto.entidades.CuotaMensual;
import com.sport.proyecto.entidades.Factura;
import com.sport.proyecto.entidades.Socio;
import com.sport.proyecto.entidades.Usuario;
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
        System.out.println(session);
        Usuario user = (Usuario) session.getAttribute("usuariosession");
        Socio socio = socioServicio.buscarSocioPorIdUsuario(user.getId())
                .orElseThrow(() -> new RuntimeException("Socio no encontrado"));

        List<CuotaMensual> cuotasNoPagadas = cuotaMensualServicio.obtenerCuotasNoPagadas(socio.getNumeroSocio());
        model.addAttribute("cuotas", cuotasNoPagadas);

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
