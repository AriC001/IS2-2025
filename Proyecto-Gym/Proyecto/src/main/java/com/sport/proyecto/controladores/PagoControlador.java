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
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/payment")
public class PagoControlador {
    @Autowired
    private PagoServicio pagoServicio;

    @GetMapping("/new")
    public String showPaymentPage(Model model) {
        return "views/payments"; // templates/payment.html
    }


    @GetMapping("/create")
    public String createPayment(@RequestParam String facturaId) throws Exception {
        String initPoint = pagoServicio.createPreference(facturaId);
        return "redirect:" + initPoint; // Redirige al checkout de Mercado Pago
    }

    @GetMapping("/success")
    public String paymentSuccess(
            @RequestParam Map<String, String> params,
            Model model) {

        // params contendrá todos los parámetros de la URL
        model.addAttribute("paymentData", params);

        return "views/payment-success"; // nombre del html Thymeleaf
    }

}
