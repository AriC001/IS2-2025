package com.example.etemplate.servicios;

import com.example.etemplate.entities.Articulo;
import com.example.etemplate.entities.Usuario;
import com.example.etemplate.repositories.ArticuloRepository;
import com.example.etemplate.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ArticuloService extends GenericServiceImpl<Articulo, String> {

    private final ArticuloRepository articuloRepository;

    public ArticuloService(ArticuloRepository repository, ArticuloRepository articuloRepository) {
        super(repository);
        this.articuloRepository = articuloRepository;
    }

    public List<Articulo> findAllActive(){
        return articuloRepository.findAllActive();
    }

}
