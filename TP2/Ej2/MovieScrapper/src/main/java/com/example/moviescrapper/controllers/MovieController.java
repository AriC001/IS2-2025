package com.example.moviescrapper.controllers;

import com.example.moviescrapper.entities.Country;
import com.example.moviescrapper.entities.Movie;
import com.example.moviescrapper.services.CountriesService;
import com.example.moviescrapper.services.MoviesService;
import com.example.moviescrapper.services.StreamingOptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/movies")
public class MovieController {

    @Autowired
    private MoviesService moviesService;

    @Autowired
    private CountriesService countriesService;

    @Autowired
    private StreamingOptionService streamingOptionService;


    public MovieController(MoviesService moviesService) {
        this.moviesService = moviesService;
    }

    @GetMapping("")
    public String listarPeliculas(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "25") int size,
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

    @GetMapping("/{id}")
    public String movie(@PathVariable String id, Model model){
        Movie m = moviesService.findById(id);
        model.addAttribute("file",m);
        List<Country> countries = countriesService.getCountries();
        model.addAttribute("countries",countries);

        return "file2";
    }


}

