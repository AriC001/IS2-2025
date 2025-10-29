package com.sport.proyecto.controladores;

import com.sport.proyecto.entidades.CuotaMensual;
import com.sport.proyecto.entidades.DetalleFactura;
import com.sport.proyecto.entidades.Factura;
import com.sport.proyecto.entidades.Usuario;
import com.sport.proyecto.servicios.*;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class FacturaControlador {
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
    private PersonaServicio personaServicio;


    @GetMapping("/facturas/{cuotaId}")
    public String verFactura(@PathVariable String cuotaId, @ModelAttribute("usuariosession") Usuario user, Model model) {

        CuotaMensual cuota = cuotaMensualServicio.buscarCuota(cuotaId);
        DetalleFactura detalleFactura = detalleFacturaServicio.buscarDetallePorCuota(cuotaId);
        Factura factura = facturaServicio.buscarFactura(detalleFactura.getFactura().getId());
        model.addAttribute("factura", factura);
        model.addAttribute("detalle", detalleFactura);
        model.addAttribute("cliente", personaServicio.buscarPorUsuario(user.getId()));

        return "views/factura";
    }
}
