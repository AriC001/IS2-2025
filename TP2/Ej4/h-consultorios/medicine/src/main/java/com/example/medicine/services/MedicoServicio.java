package com.example.medicine.services;
import com.example.medicine.errors.ErrorServicio;
import com.example.medicine.model.FotoPaciente;
import com.example.medicine.model.Medico;
import com.example.medicine.model.Paciente;
import com.example.medicine.repositories.MedicoRepositorio;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
public class MedicoServicio extends EntityServiceTemplate<Medico> {

    @Autowired
    private MedicoRepositorio medicoRepositorio;

    // Busqueda

    @Transactional
    public List<Medico> listarMedicos() throws ErrorServicio {
        try {
            return medicoRepositorio.findAll();
        } catch (Exception e) {
            throw new ErrorServicio("Error del sistema");
        }
    }
    @Transactional
    public List<Medico> listarMedicosActivos() throws ErrorServicio{
      try{
        return medicoRepositorio.findAllActives();
      } catch(ErrorServicio e){
        throw new ErrorServicio("Error del sistema");
      }
    }
    @Transactional
    public Medico buscarMedicoPorDocumento(String documento) throws ErrorServicio {
        try {
            Optional<Medico> opt = medicoRepositorio.findByDocumento(documento);
            if (opt.isPresent()) {
                return opt.get();
            } else {
                throw new ErrorServicio("No se encontro el medico solicitado");
            }
        } catch (Exception e) {
            throw new ErrorServicio("Error del sistema");
        }
    }

    @Transactional
    public Medico buscarMedico(String id) throws ErrorServicio {
        try {
            Optional<Medico> opt = medicoRepositorio.findById(id);
            if (opt.isPresent()) {
                return opt.get();
            } else {
                throw new ErrorServicio("No se encontro el medico solicitado");
            }
        } catch (Exception e) {
            throw new ErrorServicio("Error del sistema");
        }
    }

    @Transactional
    @Override
    protected void validar(Medico medico) throws ErrorServicio {
    if (medico.getNombre() == null || medico.getNombre().isEmpty()) {
      throw new ErrorServicio("El nombre del medico es obligatorio.");
    }
    if (medico.getApellido() == null || medico.getApellido().isEmpty()) {
      throw new ErrorServicio("El apellido del medico es obligatorio.");
    }
    if (medico.getDocumento() == null || medico.getDocumento().isEmpty()) {
      throw new ErrorServicio("El documento del medico es obligatorio.");
    }
    if (medico.getDocumento().length() < 7 || medico.getDocumento().length() > 8) {
      throw new ErrorServicio("El documento del medico debe tener entre 7 y 8 dígitos");
    }

    if (!medico.getDocumento().matches("\\d+")) {
      throw new ErrorServicio("El documento del medico debe contener solo números.");
    }

    if(medicoRepositorio.findByDocumento(medico.getDocumento()).isPresent()){
      throw new ErrorServicio("Ya existe un medico con ese documento.");
    }

    
  }

  @Transactional
  @Override
  public void eliminar(Medico medico) throws ErrorServicio {
    try {
      Optional<Medico> opt = medicoRepositorio.findById(medico.getId());
      if (opt.isPresent()) {
        medico.setEliminado(true);
        medicoRepositorio.save(medico);
      } else {
        throw new ErrorServicio("No se encontro el medico solicitado");
      }
    } catch (Exception e) {
      throw new ErrorServicio("Error del sistema");
    }
  }

  @Transactional
  @Override
  protected void guardar(Medico medico) throws ErrorServicio {
        medicoRepositorio.save(medico);
}

  @Transactional
  @Override
  public void actualizar(Medico medico) throws ErrorServicio {
    System.out.println("Actualizando medico: " + medico.getNombreCompleto());
    Optional<Medico> existente = medicoRepositorio.findById(medico.getId());
    if (existente.isPresent()) {
        Medico existenteMedico = existente.get();

        // Actualizamos solo los campos que se pueden modificar
        existenteMedico.setNombre(medico.getNombre());
        existenteMedico.setApellido(medico.getApellido());
        existenteMedico.setDocumento(medico.getDocumento());
          
        medicoRepositorio.save(existenteMedico);
    } else {
        throw new ErrorServicio("No se encontró el medico para actualizar");
    }
  }

  @Transactional
  public Medico buscarPorId(String id) throws ErrorServicio {
    try {
        Optional<Medico> opt = medicoRepositorio.findById(id);
        if (opt.isPresent()) {
            return opt.get();
        } else {
            throw new ErrorServicio("No se encontro el medico solicitado");
        }
    } catch (Exception e) {
        throw new ErrorServicio("Error del sistema");
    }
    }
}