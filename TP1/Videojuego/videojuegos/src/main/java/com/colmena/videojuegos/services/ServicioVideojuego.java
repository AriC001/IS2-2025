package com.colmena.videojuegos.services;

import com.colmena.videojuegos.entities.Videojuego;
import com.colmena.videojuegos.repositories.RepositorioVideojuego;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class ServicioVideojuego {
    @Autowired
    private RepositorioVideojuego videojuegoRepositorio;

    //@Override
    @Transactional
    public List<Videojuego> findAll() throws Exception {
        try {
            List<Videojuego> entities = this.videojuegoRepositorio.findAll();
            return entities;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    //@Override
    @Transactional
    public Videojuego findById(long id) throws Exception {
        try {
            Optional<Videojuego> opt = this.videojuegoRepositorio.findById(id);
            return opt.get();
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    //@Override
    @Transactional
    public Videojuego saveOne(Videojuego entity) throws Exception {
        try {
            Videojuego videojuego = this.videojuegoRepositorio.save(entity);
            return videojuego;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    //@Override
    @Transactional
    public void updateOne(Videojuego v, long id)  {
        Optional<Videojuego> opt = videojuegoRepositorio.findById(id);

        Videojuego existente = opt.get();

        // Actualizamos solo los campos que se modifican
        existente.setTitulo(v.getTitulo());
        existente.setCategoria(v.getCategoria());
        existente.setEstudio(v.getEstudio());
        if(v.getImagen() != null && !v.getImagen().isEmpty()){
            existente.setImagen(v.getImagen());
        }

        videojuegoRepositorio.save(existente);
    }

    //@Override
    @Transactional
    public boolean deleteById(long id) throws Exception {
        try {
            Optional<Videojuego> opt = this.videojuegoRepositorio.findById(id);
            if (!opt.isEmpty()) {
                Videojuego videojuego = opt.get();
                videojuego.setActivo(false);
                this.videojuegoRepositorio.save(videojuego);
            } else {
                throw new Exception();
            }
            return true;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    /*   Metodos nuevos   */

    @Transactional
    public List<Videojuego> findAllByActivo() throws Exception{
        try {
            List<Videojuego> entities = this.videojuegoRepositorio.findAllByActivo();
            return entities;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    @Transactional
    public Videojuego findByIdAndActivo(long id) throws Exception {
        try {
            Optional<Videojuego> opt = this.videojuegoRepositorio.findByIdAndActivo(id);
            return opt.get();
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    @Transactional
    public List<Videojuego> findByTitle(String q) throws Exception{
        try{
            List<Videojuego> entities = this.videojuegoRepositorio.findByTitle(q);
            return entities;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }
}