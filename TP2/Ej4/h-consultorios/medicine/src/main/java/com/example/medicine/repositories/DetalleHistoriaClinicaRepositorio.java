package com.example.medicine.repositories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.example.medicine.model.DetalleHistoriaClinica;
import java.util.List;
import com.example.medicine.model.Medico;;
@Repository
public interface DetalleHistoriaClinicaRepositorio extends JpaRepository<DetalleHistoriaClinica, String>
{
    @Query("SELECT d FROM DetalleHistoriaClinica d WHERE d.historiaClinica.id = :historiaClinicaId AND d.eliminado = false")
    List<DetalleHistoriaClinica> findByHistoriaClinicaId(String historiaClinicaId);
    @Query("SELECT d FROM DetalleHistoriaClinica d WHERE d.medico.id = :medicoId AND d.eliminado = false")
    List<DetalleHistoriaClinica> findByMedicoId(String medicoId);
    
}