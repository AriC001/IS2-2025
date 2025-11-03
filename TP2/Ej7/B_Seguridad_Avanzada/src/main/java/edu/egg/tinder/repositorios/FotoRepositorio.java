package edu.egg.tinder.repositorios;

import edu.egg.tinder.entidades.Foto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FotoRepositorio extends JpaRepository<Foto, Long> {

}
