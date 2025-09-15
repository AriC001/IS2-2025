package com.sport.proyecto.servicios;

import com.sport.proyecto.entidades.Mensaje;
import com.sport.proyecto.errores.ErrorServicio;
import com.sport.proyecto.repositorios.MensajeRepositorio;
import com.sport.proyecto.repositorios.PaisRepositorio;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MensajeServicio implements ServicioBase<Mensaje> {

    @Autowired
    private MensajeRepositorio repositorio;

    @Transactional
    @Override
    public List<Mensaje> buscarTodos() throws Exception {
        return List.of();
    }

    @Transactional
    @Override
    public Mensaje buscarPorId(Long id) throws Exception {
        Optional opt = repositorio.findById(id);
        if (opt.isPresent()) {
            Mensaje mensaje = (Mensaje) opt.get();
            return mensaje;
        } else {
            throw new ErrorServicio("No se encontro mensaje");
        }

    }

    @Override
    public void guardar(Mensaje entity) throws Exception {

    }

    @Override
    public Mensaje actualizar(Mensaje entity, Long id) throws Exception {
        return null;
    }

    @Override
    public void eliminarPorId(Long id) throws Exception {

    }
}
