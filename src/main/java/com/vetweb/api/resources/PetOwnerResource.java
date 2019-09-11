package com.vetweb.api.resources;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vetweb.api.model.PetOwner;
import com.vetweb.api.utils.GenericController;

@RestController
@RequestMapping("owners")
public class PetOwnerResource implements GenericController<PetOwner> {

	@Override
	public ResponseEntity<PetOwner> searchById(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseEntity<List<PetOwner>> searchAll() {
		// TODO Auto-generated method stub
		return null;
	}

}
