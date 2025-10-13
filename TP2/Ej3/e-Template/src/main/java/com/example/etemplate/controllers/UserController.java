package com.example.etemplate.controllers;

import com.example.etemplate.entities.Usuario;
import com.example.etemplate.servicios.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.swing.*;

@Controller
@RequestMapping("/usuarios")
public class UserController extends GenericController<Usuario, String> {

    private final UserService userService;

    @Autowired
    public UserController(UserService service, UserService userService) {
        super(service);
        this.userService = userService;
    }

    @PostMapping("/register")
    public String register(@ModelAttribute Usuario usuario) {
        System.out.println(usuario.getName() + " " + usuario.getEmail()+ " " + usuario.getPassword());
        usuario.setDeleted(false);
        service.save(usuario);
        return "redirect:/login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String email, @RequestParam String password, HttpSession session,
                        Model model){
        Usuario u = userService.findByEmail(email);

        if (u == null || !u.getPassword().equals(password)) {
            model.addAttribute("error", "Email o contraseña incorrectos");
            return "login";
        }
        session.setAttribute("usuarioLogueado", u);
        return "redirect:/index";
    }
}
