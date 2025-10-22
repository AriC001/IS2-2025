package com.practica.ej1b.controller;

import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.practica.ej1b.business.domain.entity.BaseEntity;
import com.practica.ej1b.business.logic.service.BaseService;

import jakarta.validation.Valid;

/**
 * Controlador base abstracto que implementa el patrón Template Method.
 * Proporciona operaciones CRUD comunes para vistas Thymeleaf y permite que 
 * las subclases personalicen el comportamiento específico mediante hooks.
 * Trabaja con DTOs para la vista y los convierte a entidades mediante el servicio.
 * 
 * @param <T> el tipo de entidad
 * @param <ID> el tipo del identificador
 * @param <D> el tipo del DTO
 */
public abstract class BaseController<T extends BaseEntity<ID>, ID, D> {

    protected BaseService<T, ID, D> servicio;

    public BaseController(BaseService<T, ID, D> servicio) {
        this.servicio = servicio;
    }

    /**
     * Obtiene el nombre de la entidad para las vistas y mensajes.
     * Hook method que debe ser implementado por las subclases.
     * 
     * @return el nombre de la entidad (ej: "pais", "empresa")
     */
    protected abstract String getNombreEntidad();

    /**
     * Obtiene el nombre de la vista base sin la acción.
     * Hook method que debe ser implementado por las subclases.
     * 
     * @return el path base de las vistas (ej: "pais/", "empresa/")
     */
    protected abstract String getVistaBase();

    /**
     * Crea una instancia nueva del DTO.
     * Hook method que debe ser implementado por las subclases.
     * 
     * @return una nueva instancia del DTO
     */
    protected abstract D crearDtoNuevo();

    /**
     * Convierte una entidad a DTO para mostrar en la vista.
     * Hook method que debe ser implementado por las subclases.
     * 
     * @param entidad la entidad a convertir
     * @return el DTO correspondiente
     */
    protected abstract D entidadADto(T entidad);

    /**
     * Retorna el nombre de la entidad en plural para usar en las vistas de listado.
     * Hook method que debe ser implementado por las subclases.
     * 
     * @return el nombre de la entidad en plural (ej: "paises", "localidades", "proveedores")
     */
    protected abstract String getNombreEntidadPlural();

    /**
     * Prepara los datos adicionales necesarios para el formulario.
     * Hook method que puede ser sobrescrito por las subclases.
     * 
     * @param model el modelo de Spring MVC
     * @throws Exception si ocurre un error al preparar los datos
     */
    protected void prepararDatosFormulario(Model model) throws Exception {
        // Hook vacío por defecto
    }

    /**
     * Validaciones adicionales antes de guardar.
     * Hook method que puede ser sobrescrito por las subclases.
     * 
     * @param dto el DTO a validar
     * @param model el modelo de Spring MVC
     * @throws Exception si la validación falla
     */
    protected void validacionesAdicionales(D dto, Model model) throws Exception {
        // Hook vacío por defecto
    }


    // ========== TEMPLATE METHODS ==========

    /**
     * Lista todas las entidades activas (convertidas a DTOs).
     * Template Method que define el flujo de listado.
     * 
     * @param model el modelo de Spring MVC
     * @return la vista de listado
     */
    @GetMapping("/listar")
    public String listar(Model model) {
        try {
            // Obtener entidades del servicio y convertirlas a DTOs
            var entidades = servicio.listarActivos();
            var dtos = entidades.stream()
                    .map(this::entidadADto)
                    .toList();
            model.addAttribute(getNombreEntidadPlural(), dtos);
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
        }
        return getVistaBase() + getNombreEntidad() + "-list";
    }

    /**
     * Muestra el formulario para crear una nueva entidad (con DTO nuevo).
     * Template Method que define el flujo de preparación del formulario de creación.
     * 
     * @param model el modelo de Spring MVC
     * @return la vista del formulario
     */
    @GetMapping("/nuevo")
    public String mostrarFormularioNuevo(Model model) {
        try {
            D dto = crearDtoNuevo();
            model.addAttribute(getNombreEntidad() + "DTO", dto);
            prepararDatosFormulario(model);
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
        }
        return getVistaBase() + getNombreEntidad() + "-form";
    }

    /**
     * Crea una nueva entidad desde un DTO.
     * Template Method que define el flujo de creación.
     * 
     * @param dto el DTO del formulario
     * @param br el resultado de la validación
     * @param model el modelo de Spring MVC
     * @param redirectAttributes atributos para la redirección
     * @return la vista resultante (formulario con errores o redirección)
     */
    @PostMapping("/crear")
    public String crear(@Valid @ModelAttribute D dto, BindingResult br, Model model, RedirectAttributes redirectAttributes) {
        try {
            if (br.hasErrors()) {
                model.addAttribute(getNombreEntidad() + "DTO", dto);
                prepararDatosFormulario(model);
                return getVistaBase() + getNombreEntidad() + "-form";
            }
            
            validacionesAdicionales(dto, model);
            
            // El servicio recibe el DTO y lo convierte a entidad
            servicio.crear(dto);

            redirectAttributes.addFlashAttribute("success",
                getNombreEntidad().substring(0, 1).toUpperCase() +
                getNombreEntidad().substring(1) + " creado correctamente");
            return "redirect:/" + getNombreEntidad() + "/listar";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute(getNombreEntidad() + "DTO", dto);
            try {
                prepararDatosFormulario(model);
            } catch (Exception ex) {
                model.addAttribute("error", ex.getMessage());
            }
            return getVistaBase() + getNombreEntidad() + "-form";
        }
    }

    /**
     * Muestra el formulario para editar una entidad existente (convertida a DTO).
     * Template Method que define el flujo de preparación del formulario de edición.
     * 
     * @param id el identificador de la entidad
     * @param model el modelo de Spring MVC
     * @return la vista del formulario
     */
    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable ID id, Model model) {
        try {
            T entidad = servicio.buscar(id);
            D dto = entidadADto(entidad);
            model.addAttribute(getNombreEntidad() + "DTO", dto);
            prepararDatosFormulario(model);
            return getVistaBase() + getNombreEntidad() + "-form";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "redirect:/" + getNombreEntidad() + "/listar";
        }
    }

    /**
     * Actualiza una entidad existente desde un DTO.
     * Template Method que define el flujo de actualización.
     * 
     * @param id el identificador de la entidad
     * @param dto el DTO con los nuevos datos
     * @param br el resultado de la validación
     * @param model el modelo de Spring MVC
     * @param redirectAttributes atributos para la redirección
     * @return la vista resultante (formulario con errores o redirección)
     */
    @PostMapping("/actualizar/{id}")
    public String actualizar(@PathVariable ID id, @Valid @ModelAttribute D dto, BindingResult br, Model model, RedirectAttributes redirectAttributes) {
        try {
            if (br.hasErrors()) {
                model.addAttribute(getNombreEntidad() + "DTO", dto);
                prepararDatosFormulario(model);
                return getVistaBase() + getNombreEntidad() + "-form";
            }
            
            validacionesAdicionales(dto, model);
            
            // El servicio recibe el DTO y lo mapea a la entidad existente
            servicio.modificar(id, dto);
            
            redirectAttributes.addFlashAttribute("success", 
                getNombreEntidad().substring(0, 1).toUpperCase() + 
                getNombreEntidad().substring(1) + " modificado correctamente");
            return "redirect:/" + getNombreEntidad() + "/listar";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute(getNombreEntidad() + "DTO", dto);
            try {
                prepararDatosFormulario(model);
            } catch (Exception ex) {
                model.addAttribute("error", ex.getMessage());
            }
            return getVistaBase() + getNombreEntidad() + "-form";
        }
    }

    /**
     * Elimina lógicamente una entidad.
     * Template Method que define el flujo de eliminación.
     * 
     * @param id el identificador de la entidad
     * @param redirectAttributes atributos para la redirección
     * @return la redirección al listado
     */
    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable ID id, RedirectAttributes redirectAttributes) {
        try {
            servicio.eliminar(id);
            redirectAttributes.addFlashAttribute("success", 
                getNombreEntidad().substring(0, 1).toUpperCase() + 
                getNombreEntidad().substring(1) + " eliminado correctamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/" + getNombreEntidad() + "/listar";
    }
}
