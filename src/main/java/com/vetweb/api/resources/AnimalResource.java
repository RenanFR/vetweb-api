package com.vetweb.api.resources;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vetweb.api.model.Animal;
import com.vetweb.api.service.AnimalService;
import com.vetweb.api.utils.GenericController;

@RestController
@RequestMapping("/animals")
public class AnimalResource implements GenericController<Animal> {
	
	@Autowired
	private AnimalService service;

	@Override
	public ResponseEntity<Animal> searchById(Long id) {
		return ResponseEntity.ok(service.search(id).get());
	}

	@Override
	public ResponseEntity<List<Animal>> searchAll() {
		return ResponseEntity.ok(service.list());
	}

	@Override
	public ResponseEntity<Animal> create(Animal entity) {
		return ResponseEntity.ok(service.create(entity));
	}

	@Override
	public ResponseEntity<Boolean> remove(Long id) {
		Optional<Animal> animal = service.search(id);
		if (!animal.isPresent()) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(true);
	}

}
