package com.example.moviescrapper.controllers;

import com.example.moviescrapper.entities.Movie;
import com.example.moviescrapper.services.MoviesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/movies")
public class MovieController {

    @Autowired
    private MoviesService moviesService;

    public MovieController(MoviesService moviesService) {
        this.moviesService = moviesService;
    }

    @GetMapping("")
    public String listarPeliculas(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "12") int size,
            Model model) throws IOException {

        List<Movie> todas = moviesService.getMovies();
        int total = todas.size();

        int fromIndex = (page - 1) * size;
        int toIndex = Math.min(fromIndex + size, total);

        List<Movie> paginadas = todas.subList(fromIndex, toIndex);

        int totalPages = (int) Math.ceil((double) total / size);

        model.addAttribute("movies", paginadas);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);

        return "movies"; // → nombre del template (movies.html)
    }
}

