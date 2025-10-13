package com.example.etemplate.controllers;

import com.example.etemplate.entities.Usuario;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {
    @GetMapping({(""),("/"),("index")})
    public String index(){
        return "index";
    }
    @GetMapping("contact")
    public String contact(){
        return "contact";
    }
    @GetMapping("about")
    public String about(){
        return "about";
    }
    @GetMapping("login")
    public String login(){
        return "login";
    }
    @GetMapping("register")
    public String register(Model model){
        Usuario u = new Usuario();
        model.addAttribute("user",u);
        return "register";
    }
}
