
package com.is.biblioteca.controller.view;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.is.biblioteca.business.domain.entity.Libro;
import com.is.biblioteca.business.logic.service.LibroService;

@Controller
@RequestMapping("/imagen")
public class ImagenController {
    
    @Autowired
    private LibroService libroService;
    
    @GetMapping("/perfil/{id}")
    public ResponseEntity<byte[]> imagenLibro(@PathVariable String id) {
        try {
            Libro libro = libroService.buscarLibro(id);
            
            if (libro != null && libro.getImagen() != null) {
                byte[] imagen = libro.getImagen().getContenido();
                
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.IMAGE_JPEG);
                
                return new ResponseEntity<>(imagen, headers, HttpStatus.OK);
            }
            
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
}
