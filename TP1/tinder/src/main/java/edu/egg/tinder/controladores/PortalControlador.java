package edu.egg.tinder.controladores;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class PortalControlador {
    @GetMapping("/")
    public String index(Model model) {
        return "index_1";
    }
}
