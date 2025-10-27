package com.practica.ej2consumer.controller.view;

import java.beans.PropertyEditorSupport;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.springframework.beans.propertyeditors.CustomCollectionEditor;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.practica.ej2consumer.business.domain.dto.AutorDTO;
import com.practica.ej2consumer.business.domain.dto.DocumentoDTO;
import com.practica.ej2consumer.business.domain.dto.LibroDTO;
import com.practica.ej2consumer.business.domain.dto.PersonaDTO;
import com.practica.ej2consumer.business.logic.service.AutorService;
import com.practica.ej2consumer.business.logic.service.DocumentoService;
import com.practica.ej2consumer.business.logic.service.LibroService;
import com.practica.ej2consumer.business.logic.service.PersonaService;


@Controller
@RequestMapping("/libros")
public class LibroController extends BaseController<LibroDTO, Long> {
  
  private final AutorService autorService;
  private final PersonaService personaService;
  private final DocumentoService documentoService;

  public LibroController(LibroService service, AutorService autorService, PersonaService personaService,
                        DocumentoService documentoService) {
    super(service, "libros");
    this.autorService = autorService;
    this.personaService = personaService;
    this.documentoService = documentoService;
  }

  @Override
  protected LibroDTO createNewEntity() {
    return new LibroDTO();
  }

  @ModelAttribute("autoresDisponibles")
  public List<AutorDTO> autoresDisponibles() {
    try {
      return autorService.findAllActives();
    } catch (Exception e) {
      return Collections.emptyList();
    }
  }

  @ModelAttribute("personasDisponibles")
  public List<PersonaDTO> personasDisponibles() {
    try {
      return personaService.findAllActives();
    } catch (Exception e) {
      return Collections.emptyList();
    }
  }

  @Override
  @InitBinder
  public void initBinder(WebDataBinder binder) {
    // Configurar el editor de fechas
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    dateFormat.setLenient(false);
    binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));

    binder.registerCustomEditor(PersonaDTO.class, new PropertyEditorSupport() {
      @Override
      public void setAsText(String text) {
        if (text == null || text.isBlank()) {
          setValue(null);
        } else {
          PersonaDTO persona = new PersonaDTO();
          persona.setId(Long.valueOf(text));
          setValue(persona);
        }
      }
    });

    binder.registerCustomEditor(Set.class, "autores", new CustomCollectionEditor(Set.class) {
      @Override
      @NonNull
      protected Object convertElement(@NonNull Object element) {
        if (element instanceof AutorDTO autor) {
          return autor;
        }
        String text = element.toString();
        if (text.isBlank()) {
          throw new IllegalArgumentException("El id del autor no puede estar vacío");
        }
        AutorDTO autor = new AutorDTO();
        autor.setId(Long.valueOf(text));
        return autor;
      }
    });
  }

  /**
   * Sobrescribe el método create para agregar soporte de archivos PDF.
   * Envía el libro al servidor que manejará la creación del libro y el documento
   * en una sola transacción.
   * 
   * NOTA: No usamos @Override porque agregamos un parámetro adicional (archivoPDF)
   * que no está en la clase base.
   */
  @PostMapping("/crear")
  public String crear(
      @ModelAttribute LibroDTO entity, 
      @RequestParam(value = "archivoPDF", required = false) MultipartFile archivoPDF,
      Model model) {
    
    // 🔍 LOGS DE DEBUG - CONSUMER
    System.out.println("\n=================================================");
    System.out.println("🔍 DEBUG - LibroController.create() [CONSUMER]");
    System.out.println("=================================================");
    System.out.println("📖 Título del libro: " + entity.getTitulo());
    System.out.println("📄 Archivo PDF recibido: " + (archivoPDF != null ? "SÍ" : "NO"));
    if (archivoPDF != null) {
      System.out.println("   - Nombre original: " + archivoPDF.getOriginalFilename());
      System.out.println("   - Tamaño: " + archivoPDF.getSize() + " bytes");
      System.out.println("   - Content-Type: " + archivoPDF.getContentType());
      System.out.println("   - isEmpty(): " + archivoPDF.isEmpty());
    }
    System.out.println("=================================================\n");
    
    try {
      // Si hay archivo PDF, subir el documento junto con el libro
      if (archivoPDF != null && !archivoPDF.isEmpty()) {
        System.out.println("✅ Iniciando subida de documento al servidor REST...");
        // Subir el documento al servidor
        DocumentoDTO documentoDTO = documentoService.uploadDocumento(archivoPDF, entity.getTitulo());
        System.out.println("✅ Documento subido exitosamente!");
        System.out.println("   - ID del documento: " + documentoDTO.getId());
        System.out.println("   - Nombre archivo: " + documentoDTO.getNombreArchivo());
        // Asociar el documento al libro ANTES de crear
        entity.setDocumento(documentoDTO);
      } else {
        System.out.println("⚠️  No hay archivo PDF para subir (o está vacío)");
      }
      
      // Crear el libro (con o sin documento)
      System.out.println("📚 Creando libro en el servidor REST...");
      service.create(entity);
      System.out.println("✅ Libro creado exitosamente!\n");
      
      return "redirect:/" + entityName;
    } catch (Exception ex) {
      System.err.println("\n❌ ERROR en LibroController.create():");
      System.err.println("   Mensaje: " + ex.getMessage());
      System.err.println("   Tipo: " + ex.getClass().getName());
      ex.printStackTrace();
      System.err.println();
      
      model.addAttribute("error", ex.getMessage());
      model.addAttribute("entity", entity);
      model.addAttribute("isEdit", false);
      return entityName + "/form";
    }
  }

  /**
   * Sobrescribe el método update para agregar soporte de actualización de archivos PDF.
   * Usa la ruta /actualizar/{id} para evitar conflictos con el método base.
   * 
   * NOTA: No usamos @Override porque agregamos un parámetro adicional (archivoPDF)
   * que no está en la clase base.
   */
  @PostMapping("/actualizar/{id}")
  public String actualizar(
      @PathVariable Long id, 
      @ModelAttribute LibroDTO entity,
      @RequestParam(value = "archivoPDF", required = false) MultipartFile archivoPDF,
      Model model) {
    try {
      System.out.println("\n=================================================");
      System.out.println("� DEBUG - LibroController.actualizar() [CONSUMER]");
      System.out.println("=================================================");
      System.out.println("📚 ID del libro: " + id);
      System.out.println("📖 Título del libro: " + entity.getTitulo());
      System.out.println("📄 Archivo PDF recibido: " + (archivoPDF != null ? "SÍ" : "NO"));
      
      if (archivoPDF != null) {
        System.out.println("   - Nombre original: " + archivoPDF.getOriginalFilename());
        System.out.println("   - Tamaño: " + archivoPDF.getSize() + " bytes");
        System.out.println("   - Content-Type: " + archivoPDF.getContentType());
        System.out.println("   - isEmpty(): " + archivoPDF.isEmpty());
      }
      
      System.out.println("📑 Documento actual: " + (entity.getDocumento() != null ? "SÍ" : "NO"));
      if (entity.getDocumento() != null) {
        System.out.println("   - ID Documento: " + entity.getDocumento().getId());
        System.out.println("   - Nombre: " + entity.getDocumento().getNombreArchivo());
      }
      System.out.println("=================================================\n");
      
      // Si hay un nuevo archivo PDF
      if (archivoPDF != null && !archivoPDF.isEmpty()) {
        // Verificar si el libro ya tiene un documento asociado
        if (entity.getDocumento() != null && entity.getDocumento().getId() != null) {
          // ACTUALIZAR documento existente
          System.out.println("🔄 Actualizando PDF del libro existente...");
          DocumentoDTO documentoActualizado = documentoService.actualizarDocumento(
              entity.getDocumento().getId(), 
              archivoPDF, 
              entity.getTitulo());
          entity.setDocumento(documentoActualizado);
          System.out.println("✅ PDF actualizado exitosamente");
        } else {
          // CREAR nuevo documento
          System.out.println("📄 Libro sin documento, creando uno nuevo...");
          DocumentoDTO nuevoDocumento = documentoService.uploadDocumento(archivoPDF, entity.getTitulo());
          entity.setDocumento(nuevoDocumento);
          System.out.println("✅ PDF creado y asociado exitosamente");
        }
      } else {
        System.out.println("⚠️  No se actualizará el PDF (archivo vacío o no seleccionado)");
      }
      
      service.update(id, entity);
      return "redirect:/" + entityName;
    } catch (Exception ex) {
      System.err.println("❌ Error al actualizar libro: " + ex.getMessage());
      model.addAttribute("error", ex.getMessage());
      model.addAttribute("entity", entity);
      model.addAttribute("isEdit", true);
      return entityName + "/form";
    }
  }

  @GetMapping("/buscar")
  public String buscarLibros(
      @RequestParam(required = false) String criterio, 
      @RequestParam(required = false) String valor, 
      Model model) {
    try {
      List<LibroDTO> resultados;
      
      // Si hay criterio y valor, realizar búsqueda por Strategy
      if (criterio != null && !criterio.isEmpty() && valor != null && !valor.isEmpty()) {

        System.out.println("🔍 Búsqueda - Criterio: " + criterio + ", Valor: " + valor);
        
        resultados = ((LibroService) service).buscarLibros(criterio, valor);
        model.addAttribute("criterio", criterio);
        model.addAttribute("valor", valor);
        model.addAttribute("mensajeBusqueda", "Resultados de búsqueda por " + criterio + ": \"" + valor + "\"");
      } else {
        // Sin parámetros, mostrar todos los libros
        resultados = service.findAllActives();
      }
      
      model.addAttribute("entities", resultados);
      
    } catch (Exception e) {
      e.printStackTrace();
      model.addAttribute("error", "Error al buscar: " + e.getMessage());
      model.addAttribute("entities", List.of());
    }

    return "libros/lista";
  }

}
