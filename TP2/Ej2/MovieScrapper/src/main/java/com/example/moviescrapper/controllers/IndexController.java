package com.example.moviescrapper.controllers;

import com.example.moviescrapper.entities.Country;
import com.example.moviescrapper.entities.Movie;
import com.example.moviescrapper.entities.Series;
import com.example.moviescrapper.services.CountriesService;
import com.example.moviescrapper.services.MoviesService;
import com.example.moviescrapper.services.SeriesService;
import com.example.moviescrapper.entities.StreamingOption;
import com.example.moviescrapper.services.StreamingOptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
public class IndexController {
    @Autowired
    private MoviesService moviesService;

    @Autowired
    private SeriesService seriesService;

    @Autowired
    private CountriesService countriesService;

    @Autowired
    private StreamingOptionService streamingOptionService;

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

    @GetMapping("/search")
    public String searchMovies(@RequestParam("q") String query, Model model) {
        List<Movie> results = moviesService.searchMovies(query);
        model.addAttribute("movies", results);
        model.addAttribute("query", query);
        List<Series> resultsSeries = seriesService.searchSeries(query);
        model.addAttribute("series",resultsSeries);

        return "index"; // reutiliza la misma vista de movies.html
    }

    @GetMapping("/{id}")
    public String movie(@PathVariable String id, Model model){
        System.out.println("AAAAAAAAAAAAAAAAAAAAA");
        Movie m = moviesService.findById(id);
        Series s = seriesService.findById(id);
        if(m.getId() == null){
            model.addAttribute("file",s);
        }else{
            model.addAttribute("file",m);

        }
        List<Country> countries = countriesService.getCountries();
        System.out.println(countries);
        model.addAttribute("countries",countries);
        return "file2";
    }

    //http://localhost:9000/streaming/tt0468569?country=ar
    @GetMapping("/streaming/{id}")
    @ResponseBody
    public List<StreamingOption> getStreamingOptions(@PathVariable String id,
                                                     @RequestParam String country) throws IOException, InterruptedException {

        List<StreamingOption> list = streamingOptionService.getStreaming(id, country);
        return list;
    }
}
