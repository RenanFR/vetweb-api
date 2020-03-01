package com.vetweb.api.resources;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vetweb.api.model.PetOwner;
import com.vetweb.api.service.PetOwnerService;
import com.vetweb.api.utils.GenericController;

import io.swagger.annotations.Api;

@RestController
@RequestMapping("owners")
@Api("Handles pet owner operations")
public class PetOwnerResource implements GenericController<PetOwner> {
	
	@Autowired
	private PetOwnerService service;
	
	@Override
	@GetMapping("{id}")
	public ResponseEntity<PetOwner> searchById(Long id) {
		return ResponseEntity.ok(service.searchById(id));
	}

	@Override
	@GetMapping
	public ResponseEntity<List<PetOwner>> searchAll() {
		return ResponseEntity.ok(service.searchAll());
	}

	@Override
	@PostMapping
	public ResponseEntity<PetOwner> create(@RequestBody PetOwner petOwner) {
		Long id = (long)service.create(petOwner);
		return ResponseEntity.ok(service.searchById(id));
	}

	@Override
	public ResponseEntity<Boolean> remove(Long id) {
		// TODO Auto-generated method stub
		return null;	
	}

}
