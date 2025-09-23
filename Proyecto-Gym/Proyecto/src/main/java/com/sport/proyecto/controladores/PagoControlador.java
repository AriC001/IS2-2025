package com.sport.proyecto.controladores;

import com.sport.proyecto.servicios.PagoServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequestMapping("/payment")
public class PagoControlador {
    @Autowired
    private PagoServicio pagoServicio;

    @GetMapping("/new")
    public String showPaymentPage(Model model) {
        return "views/payments"; // templates/payment.html
    }


    @PostMapping("/create")
    public String createPayment() throws Exception {
        String initPoint = pagoServicio.createPreference();
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
