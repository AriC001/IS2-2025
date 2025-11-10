package com.example.medicine.repositories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.example.medicine.model.HistoriaClinica;
import com.example.medicine.model.Usuario;
import com.example.medicine.model.Paciente;
import com.example.medicine.model.Medico;
import java.util.List;
@Repository
public interface HistoriaClinicaRepositorio extends JpaRepository<HistoriaClinica, String> {
    @Query("SELECT h FROM HistoriaClinica h WHERE h.usuario.id = :usuarioId AND h.eliminado = false")
    List<HistoriaClinica> findByUsuarioId(String usuarioId);
    @Query("SELECT h FROM HistoriaClinica h WHERE h.eliminado = false")
    List<HistoriaClinica> findAllByEliminadoFalse();
    @Query("SELECT h FROM HistoriaClinica h WHERE h.id = :id AND h.eliminado = false")
    HistoriaClinica findByIdAndEliminadoFalse(String id);
    @Query("SELECT h FROM HistoriaClinica h WHERE h.paciente.id = :pacienteId AND h.eliminado = false")
    List<HistoriaClinica> findByPacienteId(String pacienteId);
}
