package com.example.medicine.services;
import com.example.medicine.errors.ErrorServicio;
import com.example.medicine.model.FotoPaciente;
import com.example.medicine.model.Paciente;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.example.medicine.repositories.FotoPacienteRepositorio;

import java.io.IOException;
import java.util.Optional;

@Service
public class FotoPacienteServicio {
    @Autowired
    private FotoPacienteRepositorio fotoPacienteRepositorio;

    public FotoPaciente guardar(MultipartFile archivo, Paciente paciente) throws ErrorServicio {
        if (archivo != null && !archivo.isEmpty()) {
            try {
                FotoPaciente foto = new FotoPaciente();
                foto.setMime(archivo.getContentType());
                foto.setNombre(archivo.getName());
                foto.setContenido(archivo.getBytes());
                foto.setPaciente(paciente);

                return fotoPacienteRepositorio.save(foto);
            } catch (IOException e) {
                throw new ErrorServicio("Error al guardar la foto: " + e.getMessage());
            }
        } else {
            return null;
        }
    }

    public FotoPaciente actualizar(String idFoto, MultipartFile archivo) throws ErrorServicio {
        if (archivo != null && !archivo.isEmpty()) {
            try {
                Optional<FotoPaciente> opt = fotoPacienteRepositorio.findById(idFoto);
                if (opt.isPresent()) {
                    FotoPaciente foto = opt.get();
                    foto.setMime(archivo.getContentType());
                    foto.setNombre(archivo.getName());
                    foto.setContenido(archivo.getBytes());

                    return fotoPacienteRepositorio.save(foto);
                } else {
                    throw new ErrorServicio("No se encontró la foto solicitada");
                }
            } catch (IOException e) {
                throw new ErrorServicio("Error al actualizar la foto: " + e.getMessage());
            }
        } else {
            return null;
        }
    }

    public FotoPaciente buscarPorId(String id) throws ErrorServicio {
        try {
            Optional<FotoPaciente> opt = fotoPacienteRepositorio.findById(id);
            if (opt.isPresent()) {
                return opt.get();
            } else {
                throw new ErrorServicio("No se encontro la foto solicitada");
            }
        } catch (Exception e) {
            throw new ErrorServicio("Error del sistema");
        }
    }



}