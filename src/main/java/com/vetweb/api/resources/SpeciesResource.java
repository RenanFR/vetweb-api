package com.vetweb.api.resources;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vetweb.api.model.ClinicEntity;
import com.vetweb.api.model.Species;
import com.vetweb.api.service.SpeciesService;
import com.vetweb.api.utils.GenericController;

import io.swagger.annotations.Api;

@RestController
@RequestMapping("/species")
@Api(value = "Handles animal species database management operations")
public class SpeciesResource implements GenericController<Species> {
	
	@Autowired
	private SpeciesService speciesService;
	
	private Link addSelfLink(GenericController controller, ClinicEntity entity) {
		return linkTo(methodOn(controller.getClass()).searchById(entity.getEntityId())).withSelfRel();
	}
	
	private Link addListLink() {
		return linkTo(methodOn(SpeciesResource.class).searchAll()).withRel("List of all species in the database");
	}
	
	@PostMapping(consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<Species> create(@RequestBody Species species) {
		Species speciesCreated = speciesService.create(species);
		speciesCreated.add(addSelfLink(this, speciesCreated));
		return ResponseEntity.ok(speciesCreated);
	}

	
	@GetMapping("{id}")
	public ResponseEntity<Species> searchById(@PathVariable("id")Long id) {
		Species species = speciesService.searchById(id);
		species.add(addListLink());
		return ResponseEntity.ok(species);
	}
	
	@GetMapping
	public ResponseEntity<List<Species>> searchAll() {
		List<Species> all = speciesService.searchAll();
		all.forEach(s -> s.add(addSelfLink(this, s)));
		return ResponseEntity.ok(all);
	}
	
}
