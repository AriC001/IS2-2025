package com.is.biblioteca.controller.view;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.is.biblioteca.business.domain.entity.Autor;
import com.is.biblioteca.business.domain.entity.Editorial;
import com.is.biblioteca.business.domain.entity.Libro;
import com.is.biblioteca.business.logic.service.AutorService;
import com.is.biblioteca.business.logic.service.EditorialService;
import com.is.biblioteca.business.logic.service.LibroService;
import com.is.biblioteca.business.logic.service.LibroPatronesService;

@Controller
@RequestMapping("/libro")
public class LibroController {

    @Autowired
    private LibroService libroService;
    
    @Autowired
    private AutorService autorService;
    
    @Autowired
    private EditorialService editorialService;
    
    @Autowired
    private LibroPatronesService libroPatronesService;
    
    //////////////////////////////////////////
    //////////////////////////////////////////
    ///////////// VIEW: CREAR LIBRO ////////// 
    //////////////////////////////////////////
    //////////////////////////////////////////
    
    @GetMapping("/registrar")
    public String irEditAlta(ModelMap modelo) {
    	
       try {	
    	   
        List<Autor> autores = autorService.listarAutor();
        List<Editorial> editoriales = editorialService.listarEditorial();

        modelo.addAttribute("autores", autores);
        modelo.addAttribute("editoriales", editoriales);

        return "libro_form.html";
        
       }catch(Exception e) {
    	return null;   
       }
    }

    @PostMapping("/registro")
    public String aceptarEditAlta(@RequestParam(required = false) Long isbn, @RequestParam String titulo,
            			   	      @RequestParam(required = false) Integer ejemplares, @RequestParam String idAutor,
                                  @RequestParam String idEditorial, ModelMap modelo, @RequestParam(required = false) MultipartFile archivo
                                  , @RequestParam Integer anio) {
        try {

        	libroService.crearLibro(archivo, isbn, titulo, ejemplares, idAutor, idEditorial, anio);
            modelo.put("exito", "El Libro fue cargado correctamente!");

            return "redirect:/libro/lista";
            
        } catch (Exception ex) {
        	
        	List<Autor> autores = new ArrayList<Autor>();
        	List<Editorial> editoriales = new ArrayList<Editorial>();
        	try {
             autores = autorService.listarAutor();
             editoriales = editorialService.listarEditorial();
        	}catch(Exception w) {} 

            modelo.addAttribute("autores", autores);
            modelo.addAttribute("editoriales", editoriales);
            modelo.put("error", ex.getMessage());

            return "libro_form.html";
        }
        
    }
    
    //////////////////////////////////////////
    //////////////////////////////////////////
    ///////////// VIEW: LISTA LIBROS ///////// 
    //////////////////////////////////////////
    //////////////////////////////////////////

    @GetMapping("/lista")
    public String listar(ModelMap modelo) {
    	
       try {
    	   
         List<Libro> libros = libroService.listarLibro();
         modelo.addAttribute("libros", libros);

         return "libro_list";
        
       }catch(Exception e) {
    	 return null;   
       }
    }

    //////////////////////////////////////////
    //////////////////////////////////////////
    ////////// VIEW: MODIFICAR LIBRO /////////
    //////////////////////////////////////////
    //////////////////////////////////////////
    
    @GetMapping("/modificar/{isbn}")
    public String irEditModificar(@PathVariable Long isbn, ModelMap modelo) {
      
       try {
    	   
        modelo.put("libro", libroService.buscarLibroPorIsbn(isbn));
        
        List<Autor> autores = autorService.listarAutor();
        List<Editorial> editoriales = editorialService.listarEditorial();
        
        modelo.addAttribute("autores", autores);
        modelo.addAttribute("editoriales", editoriales);
        
        return "libro_modificar.html";
        
       }catch(Exception e) {
      	 return null;   
       } 
    }

    @PostMapping("/modificar/{isbn}")
    public String aceptarEditModificar(@PathVariable Long isbn, String idLibro, String titulo, Integer ejemplares,
     String idAutor, String idEditorial, ModelMap modelo, @RequestParam(required = false) MultipartFile archivo,@RequestParam Integer anio) {
        try {
        	
            List<Autor> autores = autorService.listarAutor();
            List<Editorial> editoriales = editorialService.listarEditorial();
            
            modelo.addAttribute("autores", autores);
            modelo.addAttribute("editoriales", editoriales);

            libroService.modificarLibro(archivo, idLibro, isbn, titulo, ejemplares, idAutor, idEditorial, anio);


            return "redirect:/libro/lista";

        } catch (Exception ex) {
        	
        	List<Autor> autores = new ArrayList<Autor>();
        	List<Editorial> editoriales = new ArrayList<Editorial>();
        	
        	try {
             autores = autorService.listarAutor();
             editoriales = editorialService.listarEditorial();
             modelo.put("libro", libroService.buscarLibroPorIsbn(isbn));
        	}catch(Exception e) {} 
            
            modelo.put("error", ex.getMessage());
            
            modelo.addAttribute("autores", autores);
            modelo.addAttribute("editoriales", editoriales);
            
            return "libro_modificar.html";
        }

    }
    
    //////////////////////////////////////////
    //////////////////////////////////////////
    ////////// VIEW: ELIMINAR LIBRO //////////
    //////////////////////////////////////////
    //////////////////////////////////////////
    
    @GetMapping("/eliminar/{id}")
    public String eliminarLibro(@PathVariable String id, RedirectAttributes redirectAttributes) {
        try {
            libroService.eliminarLibro(id);
            redirectAttributes.addFlashAttribute("exito", "El libro fue eliminado correctamente");
            return "redirect:/libro/lista";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al eliminar el libro: " + e.getMessage());
            return "redirect:/libro/lista";
        }
    }
    
    //////////////////////////////////////////
    //////////////////////////////////////////
    ///////// BÚSQUEDA CON STRATEGY ////////// 
    //////////////////////////////////////////
    //////////////////////////////////////////
    
    /**
     * Búsqueda de libros usando el Patrón Strategy
     * Permite buscar por: año, autor o editorial
     */
    @GetMapping("/buscar")
    public String buscarLibros(@RequestParam(required = false) String tipoBusqueda,
                               @RequestParam(required = false) String criterio,
                               ModelMap modelo) {
        try {
            List<Libro> libros;
            
            // Si no hay parámetros de búsqueda, mostrar todos los libros
            if (tipoBusqueda == null || criterio == null || criterio.trim().isEmpty()) {
                libros = libroService.listarLibro();
                modelo.addAttribute("mensaje", "Mostrando todos los libros");
            } else {
                // Usar el patrón Strategy según el tipo de búsqueda
                switch (tipoBusqueda.toLowerCase()) {
                    case "anio" -> {
                        libros = libroPatronesService.buscarLibrosPorAnio(criterio);
                        modelo.addAttribute("mensaje", "Libros del año: " + criterio);
                    }
                    case "autor" -> {
                        libros = libroPatronesService.buscarLibrosPorAutor(criterio);
                        modelo.addAttribute("mensaje", "Libros del autor: " + criterio);
                    }
                    case "editorial" -> {
                        libros = libroPatronesService.buscarLibrosPorEditorial(criterio);
                        modelo.addAttribute("mensaje", "Libros de la editorial: " + criterio);
                    }
                    default -> {
                        libros = libroService.listarLibro();
                        modelo.addAttribute("mensaje", "Tipo de búsqueda no válido. Mostrando todos los libros");
                    }
                }
            }
            
            modelo.addAttribute("libros", libros);
            modelo.addAttribute("tipoBusqueda", tipoBusqueda);
            modelo.addAttribute("criterio", criterio);
            
            return "libro_list";
            
        } catch (Exception e) {
            modelo.put("error", "Error en la búsqueda: " + e.getMessage());
            return "libro_list";
        }
    }
}
