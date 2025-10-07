package com.example.moviescrapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class StreamingAvailability {

    public void request() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://streaming-availability.p.rapidapi.com/shows/%7Btype%7D/%7Bid%7D"))
                .header("x-rapidapi-key", "354af3f7bcmsh819ceb454633f0cp10933ejsn2e2a37efe735")
                .header("x-rapidapi-host", "streaming-availability.p.rapidapi.com")
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();
        HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.body());
    }
}
