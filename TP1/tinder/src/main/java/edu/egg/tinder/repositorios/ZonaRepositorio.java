package edu.egg.tinder.repositorios;

import edu.egg.tinder.entidades.Zona;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ZonaRepositorio extends JpaRepository<Zona, Long> {
    //@Query(value = "SELECT * FROM zona z", nativeQuery = true)
    //public List<Zona> findAll();
}
