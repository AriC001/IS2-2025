package com.example.moviescrapper;
import com.example.moviescrapper.services.MoviesService;
import com.example.moviescrapper.services.SeriesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;

@Component
public class DataInitiator {


    private static MoviesService moviesService = new MoviesService();

    private static SeriesService seriesService = new SeriesService();

    public static void main() throws IOException, InterruptedException {

        File archivo1 = new File("topmovies.json");
        if(archivo1.isFile()){
            System.out.println("✅ El archivo ya existe");
        } else{
            System.out.println("❌ El archivo no existe");
            IMDB.getMovies();
        }
        File archivo2 = new File("topseries.json");
        if(archivo2.isFile()){
            System.out.println("✅ El archivo ya existe");
        } else{
            System.out.println("❌ El archivo no existe");
            IMDB.getSeries();
        }
        moviesService.loadMovies();
        seriesService.loadSeries();
    }
}
