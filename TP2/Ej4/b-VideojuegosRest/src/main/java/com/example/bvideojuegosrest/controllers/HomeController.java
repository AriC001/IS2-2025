package com.example.bvideojuegosrest.controllers;

import com.example.bvideojuegosrest.entities.Videogame;
import com.example.bvideojuegosrest.services.VideogameService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class HomeController {

    private final VideogameService videogameService;

    public HomeController(VideogameService videogameService) {
        this.videogameService = videogameService;
    }

    @GetMapping({"/", ""})
    public String index2(){
        return "index";
    }

    @GetMapping("index2")
    public String home(@RequestParam(defaultValue = "1") int page,
                       @RequestParam(defaultValue = "12") int size,
                       Model model) {
        try {
            List<Videogame> todas = videogameService.findAll();
            int total = todas.size();

            int fromIndex = Math.max(0, (page - 1) * size);
            int toIndex = Math.min(fromIndex + size, total);
            List<Videogame> paginadas = todas.subList(fromIndex, toIndex);

            int totalPages = (int) Math.ceil((double) total / size);

            model.addAttribute("entities", paginadas);
            model.addAttribute("currentPage", page);
            model.addAttribute("totalPages", totalPages);
            model.addAttribute("pageSize", size);
            model.addAttribute("entityName", "videogames");
            return "indexNoRest";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "error";
        }
    }
}
