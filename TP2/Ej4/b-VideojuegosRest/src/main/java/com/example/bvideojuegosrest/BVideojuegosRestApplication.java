package com.example.bvideojuegosrest;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
@SpringBootApplication
public class BVideojuegosRestApplication {

    public static void main(String[] args) {
        SpringApplication.run(BVideojuegosRestApplication.class, args);
    }

    // If you want SteamApi to run at startup, define a CommandLineRunner bean in a
    // configuration class or here. It's commented out by default to avoid long
    // network calls during tests. Uncomment if you want to fetch at application start.

    @Bean
    public CommandLineRunner runAtStartup(SteamApi steamApi) {
        return args -> {
            try { steamApi.getGames(); } catch (Exception e) { e.printStackTrace(); }
        };
    }

}
