package com.example.bvideojuegosrest.services;

import com.example.bvideojuegosrest.dto.SteamAppData;
import com.example.bvideojuegosrest.entities.Developer;
import com.example.bvideojuegosrest.entities.Genre;
import com.example.bvideojuegosrest.entities.Videogame;
import com.example.bvideojuegosrest.repositories.DeveloperRepository;
import com.example.bvideojuegosrest.repositories.GenreRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class VideogameService extends BaseService<Videogame,Long> {

    private List<Videogame> videogames;

    private final DeveloperRepository developerRepository;
    private final GenreRepository genreRepository;

    protected VideogameService(JpaRepository<Videogame, Long> repository,
                               DeveloperRepository developerRepository,
                               GenreRepository genreRepository) {
        super(repository);
        this.developerRepository = developerRepository;
        this.genreRepository = genreRepository;
    }

    public List<Videogame> getGames() {
        if (videogames == null) {
            try {
                loadVideogames();
            } catch (Exception e) {
                throw new RuntimeException("Error al cargar videojuegos", e);
            }
        }
        return videogames;
    }

    public void loadVideogames() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        File archivo = new File("fewGames.json");
        if (!archivo.exists()) throw new IOException("fewGames.json no encontrado");

        List<Map<String,Object>> raw = mapper.readValue(archivo, new TypeReference<List<Map<String,Object>>>(){});
        videogames = new ArrayList<>();

        for (Map<String,Object> item : raw) {
            try {
                Object dataObj = item.get("data");
                if (dataObj == null) continue;

                // convert map -> SteamAppData
                SteamAppData data = mapper.convertValue(dataObj, SteamAppData.class);
                if (data == null) continue;

                Videogame v = new Videogame();
                // Title is required by validation; if missing, skip this entry
                String title = data.getName();
                if (title == null || title.isBlank()) continue;
                v.setTitle(title);

                // Description: prefer short_description, then about_the_game; enforce length 5..100
                String desc = data.getShort_description() != null ? data.getShort_description() : data.getAbout_the_game();
                if (desc == null) desc = title; // fallback to title
                desc = desc.trim();
                if (desc.length() < 5) desc = (title.length() >= 5) ? title.substring(0, Math.min(title.length(), 205)) : (title + " - descripción");
                if (desc.length() > 250) desc = desc.substring(0, 250);
                v.setDescription(desc);

                v.setImg(data.getHeader_image());

                // Price: Steam returns cents; ensure we set at least 5 and at most 100
                float price = 0f;
                if (data.getPrice_overview() != null && data.getPrice_overview().getFinalPrice() != null) {
                    price = data.getPrice_overview().getFinalPrice() / 100f;
                }
                //if (price < 5f) price = 5f;
                //if (price > 100f) price = 100f;
                v.setPrice(price);

                // Stock default and clamp
                short stock = 10;
                v.setStock(stock);

                // Release date: if missing set to today
                if (data.getRelease_date() != null && data.getRelease_date().getDate() != null) {
                    Date parsed = tryParseDate(data.getRelease_date().getDate());
                    v.setReleaseDate(parsed != null ? parsed : new Date());
                } else {
                    v.setReleaseDate(new Date());
                }

                // developer: find or create
                if (data.getDevelopers() != null && !data.getDevelopers().isEmpty()) {
                    String devName = data.getDevelopers().get(0);
                    Optional<Developer> found = developerRepository.findByName(devName);
                    Developer dev = found.orElseGet(() -> {
                         Developer nd = new Developer(); nd.setName(devName); nd.setActive(true); return developerRepository.save(nd);
                    // Optional<Developer> found = developerRepository.findAll().stream().filter(d -> devName.equals(d.getName())).findFirst();
                    // Developer dev = found.orElseGet(() -> {
                    //     Developer nd = new Developer(); nd.setName(devName); nd.setActive(true); return developerRepository.save(nd);
                    });
                    v.setDevelopers(dev);
                }

                // genre: pick first and find or create
                if (data.getGenres() != null && !data.getGenres().isEmpty()) {
                    String genreName = data.getGenres().get(0).getDescription();
                    Genre g = genreRepository.findByName(genreName).orElseGet(() -> {
                        Genre ng = new Genre(); ng.setName(genreName); ng.setActive(true); return genreRepository.save(ng);
                    });
                    v.setGenres(g);
                }

                // persist videogame (use repository from BaseService)
                // saveOne uses repository provided to BaseService
                try {
                    saveOne(v);
                    videogames.add(v);
                } catch (Exception e) {
                    // If validation fails, log and continue with the next item
                    System.out.println("Error creando entidad: " + e.getMessage());
                }

            } catch (Exception ex) {
                System.out.println(ex.getMessage());
                continue;
            }
        }

    }

    private Date tryParseDate(String dateStr) {
        String[] patterns = new String[]{"d MMM, yyyy", "MMM d, yyyy", "yyyy-MM-dd"};
        for (String p : patterns) {
            try {
                SimpleDateFormat fmt = new SimpleDateFormat(p, Locale.ENGLISH);
                return fmt.parse(dateStr);
            } catch (ParseException ignored) {}
        }
        return null;
    }

    @Override
    protected Videogame mergeForUpdate(Videogame current, Videogame incoming) {
        if (incoming.getTitle() != null && !incoming.getTitle().isBlank()) current.setTitle(incoming.getTitle());
        if (incoming.getDescription() != null) current.setDescription(incoming.getDescription());
        if (incoming.getImg() != null) current.setImg(incoming.getImg());
        if (incoming.getPrice() > 0) current.setPrice(incoming.getPrice());
        if (incoming.getStock() > 0) current.setStock(incoming.getStock());
        if (incoming.getReleaseDate() != null) current.setReleaseDate(incoming.getReleaseDate());
        if (incoming.getDevelopers() != null) current.setDevelopers(incoming.getDevelopers());
        if (incoming.getGenres() != null) current.setGenres(incoming.getGenres());
        return current;
    }
}
