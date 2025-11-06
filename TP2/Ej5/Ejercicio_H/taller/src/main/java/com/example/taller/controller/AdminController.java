package com.example.taller.controller;
import com.example.taller.service.UsuarioService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.sound.midi.ShortMessage;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @GetMapping("/panel")
    public String panelAdminController() {
        return "adminPanel";
    }

}