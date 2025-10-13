package com.example.etemplate.controllers;

import com.example.etemplate.entities.Articulo;
import com.example.etemplate.servicios.ArticuloService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/shop")
public class ShopController {
    private final ArticuloService articuloService;

    public ShopController(ArticuloService articuloService) {
        this.articuloService = articuloService;
    }

    /*@GetMapping("")
    public String shop(){
        return "shop";
    }*/

    @GetMapping
    public String listarArticulos(Model model) {
        // Traemos todos los artículos no eliminados
        List<Articulo> articulos = articuloService.findAll().stream()
                .filter(a -> !a.isDeleted())
                .toList();

        model.addAttribute("articulos", articulos);
        return "shop";
    }
}
