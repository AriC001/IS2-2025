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
public class HistoriaClinicaServicio extends EntityServiceTemplate<HistoriaClinica> {
    @Autowired
    private HistoriaClinicaRepositorio historiaClinicaRepositorio;
    @Autowired
    private DetalleHistoriaClinicaRepositorio detalleHistoriaClinicaRepositorio;
    @Transactional
    public HistoriaClinica nuevo(Usuario usuario, Paciente paciente, Medico medico, String detalle, LocalDate fecha ) throws ErrorServicio {
        
        
        HistoriaClinica historiaClinica = new HistoriaClinica();
        historiaClinica.setUsuario(usuario);
        historiaClinica.setPaciente(paciente);
        DetalleHistoriaClinica detalleHistoriaClinica = new DetalleHistoriaClinica();
        detalleHistoriaClinica.setHistoriaClinica(historiaClinica);
        detalleHistoriaClinica.setMedico(medico);
        detalleHistoriaClinica.setFechaHistoria(fecha);
        detalleHistoriaClinica.setDetalleHistoria(detalle);
        historiaClinica.getDetallesHistoriaClinica().add(detalleHistoriaClinica);
        return historiaClinicaRepositorio.save(historiaClinica);
    }
    @Override
    @Transactional
    public void guardar(HistoriaClinica historiaClinica) {
        historiaClinicaRepositorio.save(historiaClinica);
    }
    @Override
    @Transactional
    public void validar(HistoriaClinica historiaClinica) throws ErrorServicio {
        if (historiaClinica.getPaciente() == null) {
            throw new ErrorServicio("El paciente no puede ser nulo");
        }
        if (historiaClinica.getUsuario() == null) {
            throw new ErrorServicio("El usuario no puede ser nulo");
        }
        if (historiaClinica.getDetallesHistoriaClinica().isEmpty()) {
            throw new ErrorServicio("La historia clínica debe tener al menos un detalle");
        }
        if(historiaClinica.getDetallesHistoriaClinica().stream().anyMatch(detalle -> detalle.getDetalleHistoria() == null || detalle.getDetalleHistoria().isEmpty())) {
            throw new ErrorServicio("El detalle de la historia clínica no puede ser nulo o vacío");
        }
        if(historiaClinica.getDetallesHistoriaClinica().stream().anyMatch(detalle -> detalle.getFechaHistoria() == null)) {
            throw new ErrorServicio("La fecha de la historia clínica no puede ser nula");
        }
        if (historiaClinica.getDetallesHistoriaClinica().stream().anyMatch(detalle -> detalle.getMedico() == null)) {
            throw new ErrorServicio("El médico no puede ser nulo");
        }
        if(historiaClinica.getDetallesHistoriaClinica().stream().anyMatch(detalle -> detalle.getFechaHistoria().isAfter(LocalDate.now()))) {
            throw new ErrorServicio("La fecha de la historia clínica no puede ser futura");
        }
    
    }
    @Override
    @Transactional
    public void actualizar(HistoriaClinica historiaClinica) throws ErrorServicio {
        Optional<HistoriaClinica> opt = historiaClinicaRepositorio.findById(historiaClinica.getId());
        if (opt.isPresent()) {
            HistoriaClinica historia=opt.get();
            historia.setPaciente(historiaClinica.getPaciente());
            historia.setUsuario(historiaClinica.getUsuario());
            historia.setDetallesHistoriaClinica(historiaClinica.getDetallesHistoriaClinica());
        } else {
            throw new ErrorServicio("La historia clínica no existe");
        }
    }
    @Override
    @Transactional
    public void eliminar(HistoriaClinica historiaClinica) throws ErrorServicio {
        Optional<HistoriaClinica> opt = historiaClinicaRepositorio.findById(historiaClinica.getId());
        if (opt.isPresent()) {
            HistoriaClinica historia=opt.get();
            historia.setEliminado(true);
            historiaClinicaRepositorio.save(historia);
        } else {
            throw new ErrorServicio("La historia clínica no existe");
        }
    }
    @Transactional
    public List<HistoriaClinica> listarHistoriasClinicasPorPaciente(Paciente paciente) {
        return historiaClinicaRepositorio.findByPacienteId(paciente.getId());
    }
    @Transactional
    public Optional<HistoriaClinica> buscarPorId(String id) {
        return historiaClinicaRepositorio.findById(id);
    }
    @Transactional
    public List<HistoriaClinica> listarTodasLasHistoriasClinicas() {
        return historiaClinicaRepositorio.findAll();
    }
    @Transactional
    public List<HistoriaClinica> listarActivas(){
        return historiaClinicaRepositorio.findAllByEliminadoFalse();
    }
    @Transactional
    public List<HistoriaClinica> listarHistoriasClinicasPorUsuario(Usuario usuario) {
        return historiaClinicaRepositorio.findByUsuarioId(usuario.getId());
    }
   

}
