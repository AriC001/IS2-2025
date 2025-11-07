package com.practica.nexora.ej6_e.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.practica.nexora.ej6_e.business.domain.dto.BaseDTO;
import com.practica.nexora.ej6_e.business.domain.entity.BaseEntity;
import com.practica.nexora.ej6_e.business.logic.service.BaseService;
import com.practica.nexora.ej6_e.utils.mapper.BaseMapper;

public abstract class BaseController<T extends BaseEntity<ID>, D extends BaseDTO, ID> {

  protected final BaseService<T, ID> service;
  protected final BaseMapper<T, D, ID> mapper;
  protected final String entityName;

  public BaseController(BaseService<T, ID> service, BaseMapper<T, D, ID> mapper, String entityName) {
    this.service = service;
    this.mapper = mapper;
    this.entityName = entityName;
  }

  @InitBinder
  public void initBinder(WebDataBinder binder) {
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    dateFormat.setLenient(false);
    binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
  }

  @GetMapping
  public String findAll(Model model) {
    try {
      List<T> entities = service.findAllActives();
      List<D> dtos = entities.stream()
          .map(mapper::toDTO)
          .collect(Collectors.toList());
      model.addAttribute("entities", dtos);
      model.addAttribute("entityName", entityName);
      return entityName + "/lista";
    } catch (Exception ex) {
      model.addAttribute("error", "Error al cargar la lista de " + entityName);
      return "error/500";
    }
  }

  @GetMapping("/nuevo")
  public String showCreateForm(Model model) {
    model.addAttribute("entity", createNewDTO());
    model.addAttribute("entityName", entityName);
    model.addAttribute("isEdit", false);
    return entityName + "/form";
  }

  @GetMapping("/{id}/editar")
  public String showEditForm(@PathVariable ID id, Model model) {
    try {
      Optional<T> optionalEntity = service.findById(id);
      if (optionalEntity.isPresent()) {
        D dto = mapper.toDTO(optionalEntity.get());
        model.addAttribute("entity", dto);
        model.addAttribute("entityName", entityName);
        model.addAttribute("isEdit", true);
        return entityName + "/form";
      } else {
        model.addAttribute("error", "No se encontró el " + entityName + " para editar");
        return "error/404";
      }
    } catch (Exception ex) {
      model.addAttribute("error", "No se encontró el " + entityName + " para editar");
      return "error/404";
    }
  }

  @PostMapping
  public String create(@ModelAttribute("entity") D dto, Model model) {
    try {
      T entity = mapper.toEntity(dto);
      System.out.println("Creando entidad: " + entity);
      service.save(entity);
      return "redirect:/" + entityName;
    } catch (Exception ex) {
      model.addAttribute("error", "Error al crear " + entityName + ": " + ex.getMessage());
      model.addAttribute("entity", dto);
      model.addAttribute("isEdit", false);
      return entityName + "/form";
    }
  }

  @PostMapping("/{id}")
  public String update(@PathVariable ID id, @ModelAttribute("entity") D dto, Model model) {
    try {
      T entity = mapper.toEntity(dto);
      entity.setId(id);
      service.save(entity);
      return "redirect:/" + entityName;
    } catch (Exception ex) {
      model.addAttribute("error", "Error al actualizar " + entityName + ": " + ex.getMessage());
      model.addAttribute("entity", dto);
      model.addAttribute("isEdit", true);
      return entityName + "/form";
    }
  }

  @PostMapping("/{id}/eliminar")
  public String delete(@PathVariable ID id, Model model) {
    try {
      service.delete(id);
      return "redirect:/" + entityName;
    } catch (Exception ex) {
      model.addAttribute("error", "Error al eliminar " + entityName);
      return "redirect:/" + entityName + "?error=delete";
    }
  }

  protected abstract D createNewDTO();
}