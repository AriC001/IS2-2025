package com.example.etemplate.controllers;

import com.example.etemplate.entities.Usuario;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;


@Controller
public class MainController {
    @GetMapping({(""),("/"),("index")})
    public String index(@AuthenticationPrincipal User user, Model model){
        if(user.getUsername() != null){
            model.addAttribute("username",user.getUsername());
        }
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
