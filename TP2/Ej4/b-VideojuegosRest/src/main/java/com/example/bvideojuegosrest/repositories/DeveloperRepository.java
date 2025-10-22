package com.example.bvideojuegosrest.repositories;

import com.example.bvideojuegosrest.entities.Developer;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface DeveloperRepository extends JpaRepository<Developer, Long> {
    @Query(value = "SELECT d FROM Developer d WHERE d.name = :name")
    Optional<Developer> findByName(@Param("name") String name);
}
