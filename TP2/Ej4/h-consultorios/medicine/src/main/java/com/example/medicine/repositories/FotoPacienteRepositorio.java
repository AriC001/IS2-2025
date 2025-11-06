package com.example.medicine.repositories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.medicine.model.FotoPaciente;

import java.util.List;

@Repository
public interface FotoPacienteRepositorio extends JpaRepository<FotoPaciente, String> {
    @Query(value = "SELECT * FROM foto_paciente WHERE foto_paciente.eliminado = false", nativeQuery = true )
    List<FotoPaciente> findAllByEliminadoFalse();

    @Query(value = "SELECT * FROM foto_paciente WHERE foto_paciente.paciente_id =:id AND foto_paciente.eliminado = false", nativeQuery = true)
    List<FotoPaciente> findByPacienteId(String id);

   

}
