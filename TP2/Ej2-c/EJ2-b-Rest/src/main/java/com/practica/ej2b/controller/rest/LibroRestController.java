package com.practica.ej2b.controller.rest;

import java.util.HashSet;
import java.util.Set;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.practica.ej2b.business.domain.entity.Autor;
import com.practica.ej2b.business.domain.entity.Documento;
import com.practica.ej2b.business.domain.entity.Libro;
import com.practica.ej2b.business.logic.service.AutorService;
import com.practica.ej2b.business.logic.service.DocumentoService;
import com.practica.ej2b.business.logic.service.LibroService;

@RestController
@RequestMapping("api/v1/libros")
public class LibroRestController extends BaseRestController<Libro, Long> {
    
    private final LibroService libroService;
    private final DocumentoService documentoService;
    private final AutorService autorService;
    
    public LibroRestController(LibroService service, DocumentoService documentoService, AutorService autorService) {
        super(service);
        this.libroService = service;
        this.documentoService = documentoService;
        this.autorService = autorService;
    }

    @Override
    @PostMapping
    public ResponseEntity<Libro> create(@RequestBody Libro libro) {
        try {
            // Cargar los autores persistidos desde la base de datos
            cargarAutoresPersistidos(libro);
            
            // Cargar el documento persistido si existe
            cargarDocumentoPersistido(libro);
            
            // Guardar el libro (la relación ManyToMany y OneToOne se guardarán automáticamente)
            Libro libroGuardado = libroService.save(libro);
            return ResponseEntity.ok(libroGuardado);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    @Override
    @PutMapping("/{id}")
    public ResponseEntity<Libro> update(@PathVariable Long id, @RequestBody Libro libro) {
        try {
            // Cargar los autores persistidos desde la base de datos
            cargarAutoresPersistidos(libro);
            
            // Cargar el documento persistido si existe
            cargarDocumentoPersistido(libro);
            
            // Actualizar el libro (la relación ManyToMany y OneToOne se actualizarán automáticamente)
            Libro libroActualizado = libroService.update(libro, id);
            return ResponseEntity.ok(libroActualizado);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/con-documento")
    public ResponseEntity<Libro> createWithDocument(
            @RequestBody Libro libro,
            @RequestParam(value = "archivoPDF", required = false) MultipartFile archivoPDF) {
        try {
            // Cargar los autores persistidos desde la base de datos
            cargarAutoresPersistidos(libro);
            
            // Si hay archivo PDF, crear el documento
            if (archivoPDF != null && !archivoPDF.isEmpty()) {
                Documento documento = documentoService.crearDocumentoSinGuardar(archivoPDF, libro.getTitulo());
                libro.setDocumento(documento);
                // Relación unidireccional: solo necesitamos setear el documento en el libro
            }
            
            // Guardar el libro (con cascada guardará el documento automáticamente)
            Libro libroGuardado = libroService.save(libro);
            return ResponseEntity.ok(libroGuardado);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Carga los autores persistidos desde la base de datos.
     * Este método reemplaza los autores desconectados (que solo tienen ID)
     * con las entidades completas gestionadas por JPA.
     * Esto es necesario para que las relaciones ManyToMany funcionen correctamente.
     * 
     * @param libro El libro con autores que pueden estar desconectados
     * @throws Exception Si no se puede cargar algún autor
     */
    private void cargarAutoresPersistidos(Libro libro) throws Exception {
        if (libro.getAutores() != null && !libro.getAutores().isEmpty()) {
            Set<Autor> autoresPersistidos = new HashSet<>();
            for (Autor autor : libro.getAutores()) {
                if (autor.getId() != null) {
                    // Cargar el autor completo desde la base de datos
                    Autor autorPersistido = autorService.findById(autor.getId());
                    autoresPersistidos.add(autorPersistido);
                }
            }
            // Reemplazar los autores con los persistidos
            libro.getAutores().clear();
            libro.getAutores().addAll(autoresPersistidos);
        }
    }

    /**
     * Carga el documento persistido desde la base de datos.
     * Este método reemplaza el documento desconectado (que solo tiene ID)
     * con la entidad completa gestionada por JPA.
     * Esto es necesario para que la relación OneToOne funcione correctamente.
     * 
     * @param libro El libro con documento que puede estar desconectado
     * @throws Exception Si no se puede cargar el documento
     */
    private void cargarDocumentoPersistido(Libro libro) throws Exception {
        if (libro.getDocumento() != null && libro.getDocumento().getId() != null) {
            // Cargar el documento completo desde la base de datos
            Documento documentoPersistido = documentoService.findById(libro.getDocumento().getId());
            libro.setDocumento(documentoPersistido);
            // Relación unidireccional: solo seteamos el documento en el libro
        }
    }
}
