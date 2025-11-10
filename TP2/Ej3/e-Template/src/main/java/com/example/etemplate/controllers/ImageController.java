package com.example.etemplate.controllers;

import com.example.etemplate.entities.Imagen;
import com.example.etemplate.servicios.ImageService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/imagenes")
public class ImageController {
    private final ImageService imagenService;

    public ImageController(ImageService imagenService) {
        this.imagenService = imagenService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<byte[]> obtenerImagen(@PathVariable String id) {
        return imagenService.findById(id)
                .filter(i -> !i.isDeleted())
                .map(i -> ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_TYPE, i.getMime())
                        .body(i.getContent()))
                .orElse(ResponseEntity.notFound().build());
    }
}
