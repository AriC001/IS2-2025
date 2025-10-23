package com.example.bvideojuegosrest.controllers;

import com.example.bvideojuegosrest.services.BaseService;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

/**
 * Generic base controller (Template Method) to minimize repetition in concrete controllers.
 * Subclasses should provide a `createNewEntity()` and can override hooks to change view names or model attributes.
 */
public abstract class BaseController<T, ID> {

	protected final BaseService<T, ID> service;
	protected final String entityName; // logical name used by default for views

	protected BaseController(BaseService<T, ID> service, String entityName) {
		this.service = service;
		this.entityName = entityName;
	}

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		dateFormat.setLenient(false);
		binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
	}

	/*@GetMapping({"","/","index"})
	public String index(Model model){
		try {
			model.addAttribute("entities", service.findAll());
			model.addAttribute("entityName", entityName);
			return "index";
		} catch (Exception e) {
			model.addAttribute("error", e.getMessage());
			return "error";
		}
	}*/


	@GetMapping("/list")
	public String list(Model model) {
		try {
			model.addAttribute(getEntitiesAttributeName(), service.findAll());
			model.addAttribute("entityName", entityName);
			return getListView();
		} catch (Exception e) {
			model.addAttribute("error", e.getMessage());
			return "error";
		}
	}

	@GetMapping("/{id}")
	public String detail(@PathVariable ID id, Model model) {
		try {
			T entity = service.findById(id);
			model.addAttribute(getEntityAttributeName(), entity);
			model.addAttribute("entityName", entityName);
			return getDetailView();
		} catch (Exception e) {
			model.addAttribute("error", e.getMessage());
			return "error";
		}
	}

	@GetMapping("/new")
	public String newForm(Model model) {
		model.addAttribute(getEntityAttributeName(), createNewEntity());
		model.addAttribute("entityName", entityName);
		model.addAttribute("isEdit", false);
		populateFormModel(model);
		return getFormView();
	}

	@GetMapping("/{id}/edit")
	public String editForm(@PathVariable ID id, Model model) {
		try {
			T entity = service.findById(id);
			model.addAttribute(getEntityAttributeName(), entity);
			model.addAttribute("entityName", entityName);
			model.addAttribute("isEdit", true);
			populateFormModel(model);
			return getFormView();
		} catch (Exception e) {
			model.addAttribute("error", e.getMessage());
			return "error";
		}
	}

	@PostMapping
	public String create(T entity, Model model) {
		try {
			service.saveOne(entity);
			return "redirect:" + getRedirectIndexPath();
		} catch (Exception e) {
			model.addAttribute("error", e.getMessage());
			model.addAttribute(getEntityAttributeName(), entity);
			model.addAttribute("isEdit", false);
			populateFormModel(model);
			return getFormView();
		}
	}

	@PostMapping("/{id}")
	public String update(@PathVariable ID id, T entity, Model model) {
		try {
			service.updateOne(entity, id);
			return "redirect:" + getRedirectIndexPath();
		} catch (Exception e) {
			model.addAttribute("error", e.getMessage());
			model.addAttribute(getEntityAttributeName(), entity);
			model.addAttribute("isEdit", true);
			populateFormModel(model);
			return getFormView();
		}
	}

	@PostMapping("/{id}/delete")
	public String delete(@PathVariable ID id, Model model) {
		try {
			service.deleteById(id);
			return "redirect:" + getRedirectIndexPath();
		} catch (Exception e) {
			model.addAttribute("error", e.getMessage());
			return "redirect:" + getRedirectIndexPath() + "?error=delete";
		}
	}

	// Abstracts and hooks
	protected abstract T createNewEntity();

	protected String getEntitiesAttributeName() { return "entities"; }
	protected String getEntityAttributeName() { return "entity"; }
	protected String getListView() { return "crud";}//entityName + "/list"; }
	protected String getDetailView() { return "detalle"; }
	protected String getFormView() { return entityName + "/form"; }
	protected String getRedirectIndexPath() { return "/" + entityName; }
	protected void populateFormModel(Model model) { }
}
