package com.example.etemplate.controllers;


import com.example.etemplate.entities.Articulo;
import com.example.etemplate.entities.Imagen;
import com.example.etemplate.entities.Proveedor;
import com.example.etemplate.servicios.ArticuloService;
import com.example.etemplate.servicios.ImageService;
import com.example.etemplate.servicios.ProveedorService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/articulos")
public class ArticuloController {

    private final ArticuloService articuloService;
    private final ProveedorService proveedorService;
    private final ImageService imageService;

    public ArticuloController(ArticuloService articuloService, ProveedorService proveedorService, ImageService imageService) {
        this.articuloService = articuloService;
        this.proveedorService = proveedorService;
        this.imageService = imageService;
    }

    // --- LISTAR ---
    @GetMapping
    public String listar(Model model) {
        model.addAttribute("articulos", articuloService.findAllActive());
        return "articulolistar";
    }

    // --- NUEVO ARTICULO ---
    @GetMapping("/nuevo")
    public String nuevo(Model model) {
        Articulo articulo = new Articulo();
        articulo.setProvedores(new Proveedor()); // Inicializamos la lista
        model.addAttribute("articulo", articulo);
        return "articuloguardar";
    }

    @PostMapping("/crear")
    public String crear(@ModelAttribute Articulo articulo,@RequestParam("imagenArchivo") MultipartFile archivo) throws IOException {
        // Si el ID está vacío, forzarlo a null
        if (articulo.getId() == null || articulo.getId().isBlank()) {
            articulo.setId(null);
        }

        List<Proveedor> proveedores = proveedorService.listar();
        if (!proveedores.isEmpty()) {
            articulo.setProvedores(proveedorService.listar().get(0));
        } else {
            articulo.setProvedores(new Proveedor());
        }
        articulo.setDeleted(false);

        // Guardar imagen si existe
        if (archivo != null && !archivo.isEmpty()) {
            Imagen imagen = new Imagen();
            imagen.setName(archivo.getOriginalFilename());
            imagen.setMime(archivo.getContentType());
            imagen.setContent(archivo.getBytes());
            imagen.setDeleted(false);
            imagen = imageService.save(imagen);

            List<Imagen> imagenes = new ArrayList<>();
            imagenes.add(imagen);
            articulo.setImage(imagenes);
        }

        articuloService.save(articulo);
        return "redirect:/articulos";
    }


    // --- EDITAR ARTICULO ---
    @GetMapping("/editar/{id}")
    public String editar(@PathVariable String id, Model model) {
        Articulo articulo = articuloService.findById(id).orElseThrow(() -> new IllegalArgumentException("Artículo no encontrado"));
        model.addAttribute("articulo", articulo);
        return "articuloguardar";
    }

    @PostMapping("/actualizar/{id}")
    public String actualizar(@PathVariable String id, @ModelAttribute Articulo articulo) {
        Articulo existente = articuloService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Artículo no encontrado"));

        // Copiamos los campos editables
        existente.setName(articulo.getName());
        existente.setPrice(articulo.getPrice());
        if (articulo.getProvedores() != null) {
            existente.setProvedores(articulo.getProvedores());
        }
        existente.setDeleted(articulo.isDeleted());
        existente.setImage(articulo.getImagenes());

        // Guardamos usando merge
        articuloService.save(existente);
        return "redirect:/articulos";
    }

    // --- ELIMINAR ---
    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable String id) {
        articuloService.deleteById(id);
        return "redirect:/articulos";
    }
}
