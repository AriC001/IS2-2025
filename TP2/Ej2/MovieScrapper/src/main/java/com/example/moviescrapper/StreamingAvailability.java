package com.example.moviescrapper;

import com.example.moviescrapper.entities.StreamingOption;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

public class StreamingAvailability {

    @Value("${api.token}")
    private static String apiToken;

    public static void getCountries() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://streaming-availability.p.rapidapi.com/countries?output_language=es"))
                .header("x-rapidapi-key", apiToken)
                .header("x-rapidapi-host", "streaming-availability.p.rapidapi.com")
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();
        HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        //System.out.println(response.body());
        if (response.statusCode() == 200) {
            String json = response.body();

            // Guardar el JSON completo
            File archivo = new File("countries.json");
            ObjectMapper mapper = new ObjectMapper();
            Object data = mapper.readValue(json, Object.class);
            mapper.writerWithDefaultPrettyPrinter().writeValue(archivo, data);

            System.out.println("✅ Guardado en countries.json");
        } else {
            System.out.println("❌ Error al obtener los datos: " + response.statusCode());
        }
    }

    public static List<StreamingOption> getStreaming(String id,String country) throws IOException, InterruptedException {
        List<StreamingOption> options = new ArrayList<>();

        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://streaming-availability.p.rapidapi.com/shows/" + id +
                            "?series_granularity=show&output_language=es&country=" + country))
                    .header("x-rapidapi-key", apiToken)
                    .header("x-rapidapi-host", "streaming-availability.p.rapidapi.com")
                    .GET()
                    .build();

            HttpClient client = HttpClient.newHttpClient();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                System.err.println("Error en la respuesta: " + response.statusCode());
                return options;
            }
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(response.body());

            JsonNode services = root.path("streamingOptions").path(country);

            if (services.isMissingNode() || !services.isArray() || services.size() == 0) {
                System.out.println("No hay servicios disponibles en el país: " + country);
                return options;
            }


            for (JsonNode s : services) {
                String name = s.path("service").path("name").asText();
                String link = s.path("link").asText();

                // acceder a imageSet
                JsonNode imageSet = s.path("service").path("imageSet");
                String img = "";
                if (imageSet != null) {
                    if (!imageSet.path("lightThemeImage").asText().isEmpty()) {
                        img = imageSet.path("lightThemeImage").asText();
                    } else if (!imageSet.path("darkThemeImage").asText().isEmpty()) {
                        img = imageSet.path("darkThemeImage").asText();
                    } else if (!imageSet.path("whiteImage").asText().isEmpty()) {
                        img = imageSet.path("whiteImage").asText();
                    }
                }

                if (!name.isEmpty() && !link.isEmpty()) {
                    options.add(new StreamingOption(name, link, img));
                }

            }

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        return options;
    }
}
