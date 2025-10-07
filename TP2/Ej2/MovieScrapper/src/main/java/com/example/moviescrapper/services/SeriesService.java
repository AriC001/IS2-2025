package com.example.moviescrapper.services;

import com.example.moviescrapper.entities.Series;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Service
public class SeriesService {
    private List<Series> series;

    public void loadSeries() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        File archivo = new File("topseries.json");

        series = mapper.readValue(archivo, new TypeReference<List<Series>>() {});

        //System.out.println("📽 Total de series cargadas: " + series.size());
        //series.stream().limit(5).forEach(s ->
        //        System.out.println(s.getDescription() + " (" + s.getYear() + ") ⭐ " + s.getRating())
        //);
    }


    public List<Series> getSeries() {
        if (series == null) {
            try {
                loadSeries();
            } catch (IOException e) {
                throw new RuntimeException("Error al cargar películas", e);
            }
        }
        return series;
    }
}
