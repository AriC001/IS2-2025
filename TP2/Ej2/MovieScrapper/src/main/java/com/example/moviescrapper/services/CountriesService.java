package com.example.moviescrapper.services;

import com.example.moviescrapper.entities.Country;
import com.example.moviescrapper.entities.Movie;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

@Service
public class CountriesService {

    private List<Country> countries;

    public void loadCountries() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        File archivo = new File("countries.json");

        // Mapeamos a Map<String, Country>
        Map<String, Country> map = mapper.readValue(archivo, new TypeReference<Map<String, Country>>() {});

        // Convertimos a lista si querés iterar en Thymeleaf
        countries = new ArrayList<>(map.values());

        // Opcional: ordenar por nombre
        countries.sort(Comparator.comparing(Country::getName));
    }


    public List<Country> getCountries() {
        if (countries == null) {
            try {
                loadCountries();
            } catch (IOException e) {
                throw new RuntimeException("Error al cargar películas", e);
            }
        }
        return countries;
    }
}
