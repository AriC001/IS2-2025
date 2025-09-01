package edu.egg.tinder.repositorios;

import edu.egg.tinder.entidades.Voto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VotoRepositorio extends JpaRepository<Voto, Long> {

    @Query(value = "SELECT * FROM voto v WHERE v.mascota1_id = :mascota_id ORDER BY v.fecha DESC", nativeQuery = true)
    public List<Voto> findVotesSubmittedByPet(@Param("mascota_id") Long mascota_id);

    @Query(value = "SELECT * FROM voto v WHERE v.mascota2_id = :mascota_id ORDER BY v.fecha DESC", nativeQuery = true)
    public List<Voto> findVotesReceivedByPet(@Param("mascota_id") Long mascota_id);
}
