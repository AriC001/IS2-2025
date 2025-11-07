package com.example.taller.repository;

import com.example.taller.entity.Mecanico;
import java.util.Optional;

public interface MecanicoRepository extends BaseRepository<Mecanico,String>{
	Optional<Mecanico> findByUsuarioId(String usuarioId);

}
