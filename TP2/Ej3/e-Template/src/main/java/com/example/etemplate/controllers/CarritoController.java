package com.example.etemplate.controllers;

import com.example.etemplate.entities.*;
import com.example.etemplate.servicios.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import com.example.etemplate.entities.Usuario;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;

import java.util.*;

@Controller
@RequestMapping("/carrito")
@SessionAttributes("carritoFront")
public class CarritoController {

    private final ArticuloService articuloService;
    private final CarritoService carritoService;
    private final DetalleService detalleService;
    private final UserService userService;

    @Autowired
    public CarritoController(ArticuloService articuloService, CarritoService carritoService,DetalleService detalleService,UserService userService) {
        this.articuloService = articuloService;
        this.carritoService = carritoService;
        this.detalleService = detalleService;
        this.userService = userService;
    }

    @ModelAttribute("carritoFront")
    public CarritoFront crearCarrito(HttpSession session) {
        CarritoFront carrito = (CarritoFront) session.getAttribute("carritoFront");
        if (carrito == null) {
            carrito = new CarritoFront();
            carrito.setDetallesFront(new ArrayList<>());
            session.setAttribute("carritoFront", carrito);
        }
        return carrito;
    }


    @GetMapping
    public String verCarrito(@ModelAttribute("carritoFront") CarritoFront carrito, Model model) {
        model.addAttribute("carrito", carrito);
        return "carrito";
    }

    @PostMapping("/agregar")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> agregarProducto(@RequestParam String articuloId,
                                                               @RequestParam(defaultValue = "1") int cantidad,
                                                               @ModelAttribute("carritoFront") CarritoFront carrito) {
        Map<String, Object> response = new HashMap<>();

        // Buscamos si ya existe el artículo en el carrito
        Optional<DetalleFront> existente = carrito.getDetallesFront().stream()
                .filter(d -> d.getArticuloId().equals(articuloId))
                .findFirst();

        if (existente.isPresent()) {
            // Si ya está, sumamos la cantidad
            DetalleFront detalle = existente.get();
            detalle.setCantidad(detalle.getCantidad() + cantidad);
        } else {
            Articulo articulo = new Articulo();
            Optional<Articulo> art = articuloService.findById(articuloId);
            if (art.isPresent()) {
                articulo = art.get();
                articulo.getImagenes().size();
                System.out.println(articulo.getImagenes().size());
                DetalleFront detalle = new DetalleFront(articulo.getId(), cantidad,articulo.getPrice());
                detalle.setImagenId(articulo.getImagenes().get(0).getId());
                detalle.setNombre(articulo.getName());
                //detalle.setId(UUID.randomUUID().toString());
                carrito.getDetallesFront().add(detalle);
            }
        }
        int total = carrito.getDetallesFront().stream()
                .mapToInt(DetalleFront::getCantidad)
                .sum();
        response.put("status", "ok");
        response.put("cantidad", total);
        return ResponseEntity.ok(response);
    }


    @PostMapping("/checkout")
    public String finalizarCompra(@ModelAttribute("carritoFront") CarritoFront carrito, SessionStatus status , HttpSession session, @AuthenticationPrincipal User user) {
        // Crear una nueva entidad persistente
        Usuario u = userService.findByEmail(user.getUsername());
        CarritoBack carritoBack = new CarritoBack();
        carritoBack.setUsuario(u);
        carritoBack.setTotal(carrito.getTotal());

        // Clonar los detalles
        List<Detalle> detallesPersistentes = new ArrayList<>();
        for (DetalleFront dFront : carrito.getDetallesFront()) {
            Detalle nuevo = new Detalle();
            Optional<Articulo> opt = articuloService.findById(dFront.getArticuloId());
            nuevo.setArticulo(opt.get());
            nuevo.setCantidad(dFront.getCantidad());
            nuevo.setDeleted(false);
            detallesPersistentes.add(nuevo);
        }
        carritoBack.setDetalles(detallesPersistentes);

        carritoService.save(carritoBack); // se guarda en BD

        // Limpiar sesión y asignar carrito vacío
        session.removeAttribute("carritoFront");
        status.setComplete(); // Esto indica a Spring que elimine el atributo de sesión
        CarritoFront nuevoCarrito = new CarritoFront();
        nuevoCarrito.setDetallesFront(new ArrayList<>());
        session.setAttribute("carritoFront", nuevoCarrito);

        return "redirect:/index";
    }

    @GetMapping("/cantidad")
    @ResponseBody
    public int obtenerCantidad(@ModelAttribute("carritoFront") CarritoFront carrito) {
        if (carrito.getDetallesFront() == null) return 0;

        return carrito.getDetallesFront().stream()
                .mapToInt(DetalleFront::getCantidad)
                .sum();
    }

    @GetMapping("/detalle")
    public String verCarrito(@ModelAttribute("carritoFront") CarritoFront carrito, HttpSession session,Model model){
        model.addAttribute(carrito);
        return "carrito";
    }

    @PostMapping("/eliminar")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> eliminarDetalle(@RequestParam String articuloId,
                                                               @ModelAttribute("carritoFront") CarritoFront carrito) {
        Map<String, Object> response = new HashMap<>();

        if (carrito == null || carrito.getDetallesFront() == null) {
            response.put("status", "error");
            response.put("message", "No hay carrito activo");
            return ResponseEntity.badRequest().body(response);
        }

        boolean eliminado = carrito.getDetallesFront().removeIf(
                d -> d.getArticuloId() != null && d.getArticuloId().equals(articuloId)
        );

        if (eliminado) {
            carrito.getTotal(); // recalcular total del carrito
            response.put("status", "ok");
            response.put("nuevoTotal", carrito.getTotal());
        } else {
            response.put("status", "not_found");
        }

        return ResponseEntity.ok(response);
    }



}
