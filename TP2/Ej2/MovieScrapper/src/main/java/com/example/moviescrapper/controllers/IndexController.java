package com.example.moviescrapper.controllers;

import com.example.moviescrapper.services.MoviesService;
import com.example.moviescrapper.services.SeriesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.IOException;

@Controller
public class IndexController {
    @Autowired
    private MoviesService moviesService;

    @Autowired
    private SeriesService seriesService;

    @GetMapping("")
    public String index() throws IOException {
        return "index.html";
    }
}
