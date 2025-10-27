package com.practica.ej2b.controller.rest;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.practica.ej2b.business.domain.dto.LibroDTO;
import com.practica.ej2b.business.domain.entity.Libro;
import com.practica.ej2b.business.logic.adapter.EntityDTOAdapter;
import com.practica.ej2b.business.logic.adapter.impl.LibroAdapter;
import com.practica.ej2b.business.logic.service.LibroBusquedaService;
import com.practica.ej2b.business.logic.service.LibroService;

@RestController
@RequestMapping("api/v1/libros")
public class LibroRestController extends BaseRestController<Libro, LibroDTO, Long> {

    @Autowired
    private  LibroBusquedaService busquedaService;

    @Autowired
    private  EntityDTOAdapter<Libro, LibroDTO> libroAdapter;

    public LibroRestController(LibroService service, LibroAdapter adapter) {
        super(service, adapter);
    }

    /**
     * Busca libros según el criterio especificado.
     * 
     * @param criterio El tipo de búsqueda: "titulo", "autor", o "genero"
     * @param valor El valor a buscar
     * @return Lista de LibroDTO que coinciden con la búsqueda
     */
    @GetMapping("/busqueda")
    public ResponseEntity<?> buscarLibros(
            @RequestParam(name = "criterio") String criterio,
            @RequestParam(name = "valor") String valor) {
        
        try {
            List<Libro> libros = busquedaService.buscarLibros(criterio, valor);
            
            List<LibroDTO> librosDTO = libros.stream()
                    .map(libroAdapter::toDTO)
                    .collect(Collectors.toList());
            
            return ResponseEntity.ok(librosDTO);
            
        } catch (IllegalArgumentException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Error: " + e.getMessage());
        }
    }

    /**
     * Obtiene los criterios de búsqueda disponibles.
     * 
     * @return Lista de criterios disponibles
     */
    @GetMapping("/criterios")
    public ResponseEntity<List<String>> getCriteriosDisponibles() {
        return ResponseEntity.ok(busquedaService.getCriteriosDisponibles());
    }
    
}
