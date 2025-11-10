package com.practica.ej2consumer.controller.view;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.practica.ej2consumer.business.domain.dto.BaseDTO;
import com.practica.ej2consumer.business.logic.service.BaseService;


public abstract class BaseController<T extends BaseDTO, ID> {

  protected final BaseService<T, ID> service;
  protected final String entityName;

  public BaseController(BaseService<T, ID> service, String entityName) {
    this.service = service;
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
      model.addAttribute("entities", service.findAllActives());
      model.addAttribute("entityName", entityName);
      return entityName + "/lista";
    } catch (Exception ex) {
      model.addAttribute("error", "Error al cargar la lista de " + entityName);
      return "error/500";
    }
  }

  @GetMapping("/{id}")
  public String findById(@PathVariable ID id, Model model) {
    try {
      T entity = service.findById(id);
      model.addAttribute("entity", entity);
      model.addAttribute("entityName", entityName);
      return entityName + "/detalle";
    } catch (Exception ex) {
      model.addAttribute("error", "No se encontró el " + entityName + " con ID: " + id);
      return "error/404";
    }
  }

  @GetMapping("/nuevo")
  public String showCreateForm(Model model) {
    model.addAttribute("entity", createNewEntity());
    model.addAttribute("entityName", entityName);
    model.addAttribute("isEdit", false);
    return entityName + "/form";
  }

  @GetMapping("/{id}/editar")
  public String showEditForm(@PathVariable ID id, Model model) {
    try {
      T entity = service.findById(id);
      model.addAttribute("entity", entity);
      model.addAttribute("entityName", entityName);
      model.addAttribute("isEdit", true);
      return entityName + "/form";
    } catch (Exception ex) {
      model.addAttribute("error", "No se encontró el " + entityName + " para editar");
      return "error/404";
    }
  }

  @PostMapping
  public String create(@ModelAttribute T entity, Model model) {
    try {
      service.create(entity);
      return "redirect:/" + entityName;
    } catch (Exception ex) {
      model.addAttribute("error", "Error al crear " + entityName + ": " + ex.getMessage());
      model.addAttribute("entity", entity);
      model.addAttribute("isEdit", false);
      return entityName + "/form";
    }
  }

  @PostMapping("/{id}")
  public String update(@PathVariable ID id, @ModelAttribute T entity, Model model) {
    try {
      service.update(id, entity);
      return "redirect:/" + entityName;
    } catch (Exception ex) {
      model.addAttribute("error", "Error al actualizar " + entityName + ": " + ex.getMessage());
      model.addAttribute("entity", entity);
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

  protected abstract T createNewEntity();

}
