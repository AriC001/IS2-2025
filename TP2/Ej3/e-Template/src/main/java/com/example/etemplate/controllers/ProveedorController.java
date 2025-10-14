package com.example.etemplate.controllers;

import com.example.etemplate.entities.Direccion;
import com.example.etemplate.entities.Proveedor;
import com.example.etemplate.entities.Usuario;
import com.example.etemplate.servicios.ProveedorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("proveedor")
public class ProveedorController {

    private final ProveedorService proveedorService;
    @Autowired
    public ProveedorController(ProveedorService proveedorService){
        this.proveedorService = proveedorService;
    }
    @GetMapping("")
    public String listarProveedores(Model map){
        List<Proveedor> proveedores = proveedorService.findAll();
        map.addAttribute("proveedores",proveedores);
        return "proveedorList";
    }

    @GetMapping("/{id}")
    public String verProveedor(@PathVariable String id, Model model){
        Optional<Proveedor> opt = proveedorService.findById(id);
        model.addAttribute("proveedor", opt.get());
        return "proveedor";
    }

    @GetMapping("/nuevo")
    public String crearProveedor(Model model){
        Direccion d = new Direccion();
        Proveedor p = new Proveedor();
        p.setDireccion(d);
        model.addAttribute("proveedor",p);
        return "proveedorCrear";
    }

    @PostMapping("/nuevo")
    public String guardarProveedor(@ModelAttribute Proveedor proveedor){
        if (proveedor.getId() == null || proveedor.getId().isBlank()) {
            proveedor.setId(null); // <--- forzar que Hibernate genere un nuevo UUID
        }
        proveedorService.save(proveedor);
        return "proveedor";
    }

    @GetMapping("/editar/{id}")
    public String editarProveedor(@PathVariable String id, Model model) {
        Optional<Proveedor> opt = proveedorService.findById(id);
        Proveedor proveedor = opt.get();
        if (proveedor.getDireccion() == null) {
            proveedor.setDireccion(new Direccion());
        }
        model.addAttribute("proveedor", proveedor);
        return "proveedorEditar";
    }

    // 📌 Actualizar proveedor existente
    @PostMapping("/actualizar/{id}")
    public String actualizarProveedor(@PathVariable String id, @ModelAttribute Proveedor proveedorActualizado) {
        Optional<Proveedor> opt = proveedorService.findById(id);
        Proveedor proveedorExistente = opt.get();

        if (proveedorExistente != null) {
            proveedorExistente.setName(proveedorActualizado.getName());
            if (proveedorExistente.getDireccion() == null) {
                proveedorExistente.setDireccion(new Direccion());
            }

            proveedorExistente.getDireccion().setLatitud(proveedorActualizado.getDireccion().getLatitud());
            proveedorExistente.getDireccion().setLongitud(proveedorActualizado.getDireccion().getLongitud());

            proveedorService.save(proveedorExistente);
        }

        return "redirect:/proveedor";
    }
}
