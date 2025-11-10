package com.example.bvideojuegosrest.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import com.example.bvideojuegosrest.entities.Videogame;
import java.util.Objects;
 
import com.example.bvideojuegosrest.services.VideogameService;

@Controller
@RequestMapping("/videogames")
public class VideogameController extends BaseController<Videogame, Long> {

    @SuppressWarnings("unused")
    private final VideogameService videogameService;

    protected VideogameController(VideogameService videogameService) {
        super(videogameService, "videogames");
        Objects.requireNonNull(videogameService, "videogameService");
        this.videogameService = videogameService;
    }

    @GetMapping("/list")
    public String list(Model model){
        // Delegar al método list() del BaseController para reutilizar la lógica común
        return super.list(model);
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model){
        return super.detail(id,model);
    }



    @Override
    protected Videogame createNewEntity() {
        return new Videogame();
    }
    
}
