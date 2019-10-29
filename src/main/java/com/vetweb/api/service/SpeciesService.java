package com.vetweb.api.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vetweb.api.model.Species;
import com.vetweb.api.persist.SpeciesRepository;

@Service
public class SpeciesService {
	
	@Autowired
	private SpeciesRepository speciesRepository;
	
	public Species create(Species species) {
		return speciesRepository.save(species);
	}
	
	public Optional<Species> searchById(Long id) {
		return speciesRepository.findById(id);
	}
	
	public List<Species> searchAll() {
		return speciesRepository.findAll();
	}

}
