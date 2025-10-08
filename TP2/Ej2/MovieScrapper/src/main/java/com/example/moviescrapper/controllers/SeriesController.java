package com.example.moviescrapper.controllers;

import com.example.moviescrapper.entities.Country;
import com.example.moviescrapper.entities.Movie;
import com.example.moviescrapper.entities.Series;
import com.example.moviescrapper.services.CountriesService;
import com.example.moviescrapper.services.SeriesService;
import com.example.moviescrapper.services.StreamingOptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/series")
public class SeriesController {

    @Autowired
    private SeriesService seriesService;

    @Autowired
    private CountriesService countriesService;

    @Autowired
    private StreamingOptionService streamingOptionService;

    @GetMapping("")
    public String listarseries(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "25") int size,
            Model model) throws IOException {

        List<Series> todas = seriesService.getSeries();
        int total = todas.size();

        int fromIndex = (page - 1) * size;
        int toIndex = Math.min(fromIndex + size, total);

        List<Series> paginadas = todas.subList(fromIndex, toIndex);

        int totalPages = (int) Math.ceil((double) total / size);

        model.addAttribute("series", paginadas);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);

        return "series"; // → nombre del template (series.html)
    }

    @GetMapping("/{id}")
    public String series(@PathVariable String id, Model model){
        Series s = seriesService.findById(id);
        model.addAttribute("file",s);
        List<Country> countries = countriesService.getCountries();
        model.addAttribute("countries",countries);
        return "file2";
    }
}

