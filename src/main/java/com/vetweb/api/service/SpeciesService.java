package com.vetweb.api.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vetweb.api.model.Species;
import com.vetweb.api.persist.auth.SpeciesRepository;

@Service
public class SpeciesService {
	
	@Autowired
	private SpeciesRepository speciesRepository;
	
	public Species create(Species species) {
		return speciesRepository.save(species);
	}
	
	public Species searchById(Long id) {
		return speciesRepository.findById(id).get();
	}
	
	public List<Species> searchAll() {
		return speciesRepository.findAll();
	}

}
