package com.vetweb.api.resources;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vetweb.api.model.EntityType;
import com.vetweb.api.model.Species;
import com.vetweb.api.service.SpeciesService;
import com.vetweb.api.utils.GenericController;

import io.swagger.annotations.Api;

@RestController
@RequestMapping("/species")
@Api(value = "Handles animal species database management operations")
public class SpeciesResource implements GenericController<Species> {
	
	@Autowired
	private SpeciesService service;
	
	@Override
	@PostMapping(consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<Species> create(@RequestBody Species species) {
		Species speciesCreated = service.create(species);
		speciesCreated.add(addSelfLink(this, speciesCreated));
		return ResponseEntity.ok(speciesCreated);
	}
	
	@Override
	@GetMapping("{id}")
	public ResponseEntity<Species> searchById(@PathVariable("id")Long id) {
		Species species = service.searchById(id).get();
		species.add(addListLink(this, EntityType.SPECIES));
		return ResponseEntity.ok(species);
	}
	
	@Override
	@GetMapping
	public ResponseEntity<List<Species>> searchAll() {
		List<Species> all = service.searchAll();
		all.forEach(s -> s.add(addSelfLink(this, s)));
		return ResponseEntity.ok(all);
	}

	@Override
	public ResponseEntity<Boolean> remove(Long id) {
		Optional<Species> species = service.searchById(id);
		if (!species.isPresent()) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(true);
	}

}
