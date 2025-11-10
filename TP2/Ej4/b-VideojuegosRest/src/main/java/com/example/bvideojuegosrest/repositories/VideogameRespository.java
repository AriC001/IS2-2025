package com.example.bvideojuegosrest.repositories;

import com.example.bvideojuegosrest.entities.Videogame;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VideogameRespository extends JpaRepository<Videogame, Long> {
}
