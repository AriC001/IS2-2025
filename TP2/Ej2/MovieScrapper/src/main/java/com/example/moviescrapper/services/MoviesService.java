package com.example.moviescrapper.services;

import com.example.moviescrapper.entities.Movie;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.util.List;

@Service
public class MoviesService {

    private List<Movie> movies;

    public List<Movie> getMovies() {
        if (movies == null) {
            try {
                loadMovies();
            } catch (IOException e) {
                throw new RuntimeException("Error al cargar películas", e);
            }
        }
        return movies;
    }

    public void loadMovies() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        File archivo = new File("topmovies.json");

        movies = mapper.readValue(archivo, new TypeReference<List<Movie>>() {});

        System.out.println("📽 Total de películas cargadas: " + movies.size());
        movies.stream().limit(5).forEach(m ->
                System.out.println(m.getDescription() + " (" + m.getYear() + ") ⭐ " + m.getRating())
        );
    }

}
