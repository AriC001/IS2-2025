package com.example.etemplate.controllers;

import com.example.etemplate.entities.*;
import com.example.etemplate.servicios.ArticuloService;
import com.example.etemplate.servicios.CarritoService;
import com.example.etemplate.servicios.GenericService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import com.example.etemplate.entities.Usuario;
import com.example.etemplate.servicios.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/carrito")
@SessionAttributes("carritoFront")
public class CarritoController {

    private final ArticuloService articuloService;
    private final CarritoService carritoService;

    @Autowired
    public CarritoController(ArticuloService articuloService, CarritoService carritoService) {
        this.articuloService = articuloService;
        this.carritoService = carritoService;
    }

    @ModelAttribute("carritoFront")
    public CarritoFront crearCarrito(HttpSession session) {
        CarritoFront carrito = new CarritoFront(); // el carrito se guarda en sesión
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        carrito.setUsuario(usuario); // ahora el carrito sabe quién es
        return carrito;
    }

    @GetMapping
    public String verCarrito(@ModelAttribute("carritoFront") CarritoFront carrito, Model model) {
        model.addAttribute("carrito", carrito);
        return "carrito";
    }

    @PostMapping("/agregar")
    public String agregarProducto(@RequestParam String idArticulo,
                                  @RequestParam(defaultValue = "1") int cantidad,
                                  @ModelAttribute("carritoFront") CarritoFront carrito) {
        // Lógica para buscar el artículo
        Articulo articulo = new Articulo();
        Optional<Articulo> art = articuloService.findById(idArticulo);
        if(art.isPresent()){
            articulo = art.get();
        }
        carrito.getDetalles().add(new Detalle(articulo, 1)); // ejemplo
        return "redirect:/carrito";
    }

    @PostMapping("/checkout")
    public String finalizarCompra(@ModelAttribute("carritoFront") CarritoFront carrito, HttpSession session) {
        carritoService.save(carrito); // se guarda en BD
        session.removeAttribute("carritoFront"); // limpiar la sesión
        return "redirect:/index";
    }


}
