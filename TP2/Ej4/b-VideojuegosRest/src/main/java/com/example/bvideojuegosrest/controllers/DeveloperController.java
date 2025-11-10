package com.example.bvideojuegosrest.controllers;

import com.example.bvideojuegosrest.entities.Developer;
import com.example.bvideojuegosrest.services.DeveloperService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.ui.Model;

@Controller
@RequestMapping("/developers")
public class DeveloperController extends BaseController<Developer, Long> {

	public DeveloperController(DeveloperService service) {
		super(service, "developers");
	}

	@Override
	protected Developer createNewEntity() {
		return new Developer();
	}

	@Override
	protected String getListView() { return "views/developers/list"; }

	@Override
	protected String getDetailView() { return "views/developers/detail"; }

	@Override
	protected String getFormView() { return "views/developers/form"; }

	@Override
	protected void populateFormModel(Model model) {
		// no extra data for developer form
	}
}
