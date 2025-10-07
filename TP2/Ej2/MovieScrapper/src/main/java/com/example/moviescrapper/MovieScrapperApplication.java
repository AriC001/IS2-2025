package com.example.moviescrapper;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

@SpringBootApplication
public class MovieScrapperApplication {

    public static void main(String[] args) throws IOException, InterruptedException {

        SpringApplication.run(MovieScrapperApplication.class, args);
        DataInitiator.main();
    }

}
