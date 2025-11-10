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
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.practica.ej2consumer.business.domain.dto.AutorDTO;
import com.practica.ej2consumer.business.domain.dto.LibroDTO;
import com.practica.ej2consumer.business.domain.dto.PersonaDTO;
import com.practica.ej2consumer.business.logic.service.AutorService;
import com.practica.ej2consumer.business.logic.service.LibroService;
import com.practica.ej2consumer.business.logic.service.PersonaService;

@Controller
@RequestMapping("/libros")
public class LibroController extends BaseController<LibroDTO, Long> {
  
  private final AutorService autorService;
  private final PersonaService personaService;

  public LibroController(LibroService service, AutorService autorService, PersonaService personaService) {
    super(service, "libros");
    this.autorService = autorService;
    this.personaService = personaService;
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
          throw new IllegalArgumentException("Autor id cannot be blank");
        }
        AutorDTO autor = new AutorDTO();
        autor.setId(Long.valueOf(text));
        return autor;
      }
    });
  }

}
