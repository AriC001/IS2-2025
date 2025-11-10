package com.example.moviescrapper;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;

import java.io.File;

public class IMDB {

    @Value("${api.token}")
    private static String apiToken;

        public static void getMovies() throws IOException, InterruptedException {
            HttpRequest topMovies = HttpRequest.newBuilder()
                    .uri(URI.create("https://imdb236.p.rapidapi.com/api/imdb/top250-movies"))
                    .header("x-rapidapi-key", apiToken)
                    .header("x-rapidapi-host", "imdb236.p.rapidapi.com")
                    .method("GET", HttpRequest.BodyPublishers.noBody())
                    .build();

            HttpResponse<String> topMoviesResponse = HttpClient.newHttpClient()
                    .send(topMovies, HttpResponse.BodyHandlers.ofString());


            if (topMoviesResponse.statusCode() == 200) {
                String json = topMoviesResponse.body();

                // Guardar el JSON completo
                File archivo = new File("topmovies.json");
                ObjectMapper mapper = new ObjectMapper();
                Object data = mapper.readValue(json, Object.class);
                mapper.writerWithDefaultPrettyPrinter().writeValue(archivo, data);

                System.out.println("✅ Guardado en topmovies.json");
            } else {
                System.out.println("❌ Error al obtener los datos: " + topMoviesResponse.statusCode());
            }

        }
            //System.out.println(topMoviesResponse.statusCode());
            //System.out.println(topMoviesResponse.body());
        public static void getSeries() throws IOException, InterruptedException {
            HttpRequest topSeries = HttpRequest.newBuilder()
                    .uri(URI.create("https://imdb236.p.rapidapi.com/api/imdb/top250-tv"))
                    .header("x-rapidapi-key", apiToken)
                    .header("x-rapidapi-host", "imdb236.p.rapidapi.com")
                    .method("GET", HttpRequest.BodyPublishers.noBody())
                    .build();

            HttpResponse<String> topSeriesResponse = HttpClient.newHttpClient()
                    .send(topSeries, HttpResponse.BodyHandlers.ofString());

            if (topSeriesResponse.statusCode() == 200) {
                String json = topSeriesResponse.body();

                // Guardar el JSON completo
                File archivo = new File("topseries.json");
                ObjectMapper mapper = new ObjectMapper();
                Object data = mapper.readValue(json, Object.class);
                mapper.writerWithDefaultPrettyPrinter().writeValue(archivo, data);

                System.out.println("✅ Guardado en topseries.json");
            } else {
                System.out.println("❌ Error al obtener los datos: " + topSeriesResponse.statusCode());
            }
        }
    }

