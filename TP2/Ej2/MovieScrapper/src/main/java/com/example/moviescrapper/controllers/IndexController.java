package com.example.moviescrapper.controllers;

import com.example.moviescrapper.entities.Movie;
import com.example.moviescrapper.entities.Series;
import com.example.moviescrapper.services.MoviesService;
import com.example.moviescrapper.services.SeriesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.IOException;
import java.util.List;

@Controller
public class IndexController {
    @Autowired
    private MoviesService moviesService;

    @Autowired
    private SeriesService seriesService;

    @GetMapping("")
    public String index(Model model) throws IOException {

        List<Movie> todas = moviesService.getMovies();
        int total = todas.size();
        List<Movie> paginadas = todas.subList(1, 12);

        List<Series> todasSeries = seriesService.getSeries();
        int totalSeries = todasSeries.size();
        List<Series> paginadasSeries = todasSeries.subList(1, 12);

        model.addAttribute("movies", paginadas);
        model.addAttribute("series", paginadasSeries);


        return "index.html";
    }
}
