package com.sport.proyecto.servicios;

import org.springframework.stereotype.Service;
import com.sport.proyecto.entidades.Empleado;
import com.sport.proyecto.repositorios.EmpleadoRepositorio;

import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
@Service
public class EmpleadoServicio {
    @Autowired
    private EmpleadoRepositorio empleadoRepositorio;

    @Transactional
    public Empleado buscarEmpleadoPorIdUsuario(String idUsuario) {
        return empleadoRepositorio.findEmpleadoByIdUsuario(idUsuario);
    }
    @Transactional
    public List<Empleado> obtenerProfesores() {
        return empleadoRepositorio.findAllProfesores();
    }

}
