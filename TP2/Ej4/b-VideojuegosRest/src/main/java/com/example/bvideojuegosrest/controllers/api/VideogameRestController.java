package com.example.bvideojuegosrest.controllers.api;

import com.example.bvideojuegosrest.dto.VideogameDto;
import com.example.bvideojuegosrest.entities.Developer;
import com.example.bvideojuegosrest.entities.Genre;
import com.example.bvideojuegosrest.entities.Videogame;
import com.example.bvideojuegosrest.repositories.DeveloperRepository;
import com.example.bvideojuegosrest.repositories.GenreRepository;
import com.example.bvideojuegosrest.services.VideogameService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/videogames")
public class VideogameRestController {

    private final VideogameService service;
    private final DeveloperRepository developerRepository;
    private final GenreRepository genreRepository;

    public VideogameRestController(VideogameService service, DeveloperRepository developerRepository, GenreRepository genreRepository) {
        this.service = service;
        this.developerRepository = developerRepository;
        this.genreRepository = genreRepository;
    }

    private VideogameDto toDto(Videogame v) {
        if (v == null) return null;
        return new VideogameDto(v.getId(), v.getTitle(), v.getDescription(), v.getImg(), v.getPrice(), v.getStock(), v.getReleaseDate(),
                v.getDevelopers() != null ? v.getDevelopers().getName() : null,
                v.getGenres() != null ? v.getGenres().getName() : null);
    }

    private Videogame fromDto(VideogameDto dto) {
        if (dto == null) return null;
        Videogame v = new Videogame();
        v.setTitle(dto.getTitle());
        v.setDescription(dto.getDescription());
        v.setImg(dto.getImg());
        v.setPrice(dto.getPrice());
        v.setStock(dto.getStock());
        v.setReleaseDate(dto.getReleaseDate());
        if (dto.getDeveloperName() != null) {
            Optional<Developer> dev = developerRepository.findByName(dto.getDeveloperName());
            v.setDevelopers(dev.orElseGet(() -> {
                Developer d = new Developer(); d.setName(dto.getDeveloperName()); d.setActive(true); return developerRepository.save(d);
            }));
        }
        if (dto.getGenreName() != null) {
            Genre g = genreRepository.findByName(dto.getGenreName()).orElseGet(() -> {
                Genre ng = new Genre(); ng.setName(dto.getGenreName()); ng.setActive(true); return genreRepository.save(ng);
            });
            v.setGenres(g);
        }
        return v;
    }

    @GetMapping
    public ResponseEntity<Page<VideogameDto>> list(@RequestParam(defaultValue = "0") int page,
                                                   @RequestParam(defaultValue = "12") int size) {
        try {
            List<Videogame> all = service.findAll();
            int start = Math.min(page * size, all.size());
            int end = Math.min(start + size, all.size());
            List<VideogameDto> dtos = all.subList(start, end).stream().map(this::toDto).collect(Collectors.toList());
            Page<VideogameDto> p = new PageImpl<>(dtos, PageRequest.of(page, size), all.size());
            return ResponseEntity.ok(p);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<VideogameDto> get(@PathVariable Long id) {
        try {
            Videogame v = service.findById(id);
            return ResponseEntity.ok(toDto(v));
        } catch (Exception e) {
            String msg = e.getMessage() != null ? e.getMessage().toLowerCase() : "";
            if (msg.contains("no existe") || msg.contains("no existe la entidad")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping
    public ResponseEntity<VideogameDto> create(@RequestBody VideogameDto dto) {
        try {
            Videogame v = fromDto(dto);
            Videogame saved = service.saveOne(v);
            return ResponseEntity.created(URI.create("/api/v1/videogames/" + saved.getId())).body(toDto(saved));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<VideogameDto> update(@PathVariable Long id, @RequestBody VideogameDto dto) {
        try {
            Videogame incoming = fromDto(dto);
            Videogame saved = service.updateOne(incoming, id);
            return ResponseEntity.ok(toDto(saved));
        } catch (Exception e) {
            String msg = e.getMessage() != null ? e.getMessage().toLowerCase() : "";
            if (msg.contains("no existe") || msg.contains("no existe la entidad")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        try {
            service.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            String msg = e.getMessage() != null ? e.getMessage().toLowerCase() : "";
            if (msg.contains("no existe") || msg.contains("no existe la entidad")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
