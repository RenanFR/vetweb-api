package com.vetweb.api.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vetweb.api.model.PetOwner;
import com.vetweb.api.persist.PetOwnerRepository;

@Service
public class PetOwnerService {
	
	@Autowired
	private PetOwnerRepository repository;
	
	public Integer create(PetOwner petOwner) {
		return repository.create(petOwner);
	}
	
	public PetOwner searchById(Long id) {
		return repository.searchById(id);
	}
	
	public List<PetOwner> searchAll() {
		return repository.searchAll();
	}

}
