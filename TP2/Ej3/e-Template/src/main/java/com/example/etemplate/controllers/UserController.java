package com.example.etemplate.controllers;

import com.example.etemplate.entities.CarritoBack;
import com.example.etemplate.entities.CarritoFront;
import com.example.etemplate.entities.CarritoTemplate;
import com.example.etemplate.entities.Usuario;
import com.example.etemplate.servicios.CarritoService;
import com.example.etemplate.servicios.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.swing.*;
import java.util.List;

@Controller
@RequestMapping("/usuarios")
public class UserController extends GenericController<Usuario, String> {

    private final UserService userService;
    private final CarritoService carritoService;

    @Autowired
    public UserController(UserService service, UserService userService, CarritoService carritoService) {
        super(service);
        this.userService = userService;
        this.carritoService = carritoService;
    }

    @PostMapping("/register")
    public String register(@ModelAttribute Usuario usuario) {
        System.out.println(usuario.getName() + " " + usuario.getEmail()+ " " + usuario.getPassword());
        usuario.setDeleted(false);
        service.save(usuario);
        return "redirect:/login";
    }

    /*
    @PostMapping("/login")
    public String login(@RequestParam String email, @RequestParam String password, HttpSession session,
                        Model model){
        Usuario u = userService.findByEmail(email);

        if (u == null || !u.getPassword().equals(password)) {
            model.addAttribute("error", "Email o contraseña incorrectos");
            return "login";
        }
        return "redirect:/index";
    }*/

    @GetMapping("/perfil")
    public String perfil(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        Usuario u = userService.findByEmail(userDetails.getUsername());
        model.addAttribute("usuario", u);
        List<CarritoBack> carritos = carritoService.findByUsuario(u.getId());
        model.addAttribute("carritos", carritos);
        return "perfil";
    }
}
