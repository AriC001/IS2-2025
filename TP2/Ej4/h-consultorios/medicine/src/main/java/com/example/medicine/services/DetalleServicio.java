package com.example.medicine.services;
import com.example.medicine.model.HistoriaClinica;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.medicine.repositories.HistoriaClinicaRepositorio;
import com.example.medicine.services.EntityServiceTemplate;
import jakarta.transaction.Transactional;
import com.example.medicine.errors.ErrorServicio;
import com.example.medicine.model.Usuario;
import com.example.medicine.model.Paciente;
import com.example.medicine.model.Medico;
import com.example.medicine.model.DetalleHistoriaClinica;
import com.example.medicine.repositories.DetalleHistoriaClinicaRepositorio;
import com.example.medicine.repositories.HistoriaClinicaRepositorio;
import java.util.List;
import java.util.Optional;
import jakarta.transaction.Transactional;
@Service
public class DetalleServicio extends EntityServiceTemplate<DetalleHistoriaClinica> {
    @Autowired
    private DetalleHistoriaClinicaRepositorio detalleHistoriaClinicaRepositorio;
    @Autowired
    private HistoriaClinicaRepositorio historiaClinicaRepositorio;

    @Transactional
    @Override
    public void guardar(DetalleHistoriaClinica detalle){
        detalleHistoriaClinicaRepositorio.save(detalle);
    }
    @Transactional
    @Override
    public void actualizar(DetalleHistoriaClinica detalle) throws ErrorServicio{
        Optional<DetalleHistoriaClinica> existente=detalleHistoriaClinicaRepositorio.findById(detalle.getId());
        if(existente.isPresent()){
            DetalleHistoriaClinica d = existente.get();
            d.setMedico(detalle.getMedico());
            d.setDetalleHistoria(detalle.getDetalleHistoria());
            d.setFechaHistoria(detalle.getFechaHistoria());
        }
        else{
            throw new ErrorServicio("Error actualizando detalles");
        }
    
        
    }
    @Transactional
    @Override
    public void validar(DetalleHistoriaClinica detalleH) throws ErrorServicio{
        if(detalleH.getDetalleHistoria()==null || detalleH.getDetalleHistoria().isEmpty()){
            throw new ErrorServicio("El detalle no puede ser nulo");
        }
        if(detalleH.getFechaHistoria().isAfter(LocalDate.now())|| detalleH.getFechaHistoria()==null){
            throw new ErrorServicio("Fecha inválida");
        } 
        if(detalleH.getMedico()==null || detalleH.getMedico().getId()==null || detalleH.getMedico().isEliminado()){
            throw new ErrorServicio("Error con el médico");
        }
    }
    @Transactional
    @Override
    public void eliminar(DetalleHistoriaClinica detalleHistoria){
        Optional<DetalleHistoriaClinica> dOptional = detalleHistoriaClinicaRepositorio.findById(detalleHistoria.getId());
        if (dOptional.get()!=null){
            DetalleHistoriaClinica d  =dOptional.get();
            d.setEliminado(true);
            detalleHistoriaClinicaRepositorio.save(d);
        }
    }
}

