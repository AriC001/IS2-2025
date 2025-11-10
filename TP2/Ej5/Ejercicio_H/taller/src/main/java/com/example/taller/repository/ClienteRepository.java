package com.example.taller.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.*;
import com.example.taller.entity.Cliente;
@Repository
public interface ClienteRepository extends BaseRepository<Cliente, String> {

    @Query("SELECT c FROM Cliente c WHERE c.documento = :documento AND c.eliminado = false")
    Optional<Cliente> findByDocumento(String documento);

    @Query("SELECT c FROM Cliente c WHERE c.eliminado = false")
    List<Cliente> findAllActivos();
} 
