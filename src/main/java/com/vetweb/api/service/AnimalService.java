package com.vetweb.api.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vetweb.api.model.Animal;
import com.vetweb.api.model.MedicalRecord;
import com.vetweb.api.persist.AnimalRepository;

@Service
public class AnimalService {
	
	@Autowired
	private AnimalRepository repository;
	
	@Autowired
	private MedicalRecordService medicalRecordService;
	
	public Animal create(Animal animal) {
		return medicalRecordService
			.create(MedicalRecord
			.builder()
			.animal(repository.save(animal))
			.build()).getAnimal();
	}
	
	public List<Animal> list() {
		return repository.findAll();
	}
	
	public Optional<Animal> search(Long identifier) {
		return repository.findById(identifier);
	}
	
	public void remove(Animal animal) {
		repository.delete(animal);
	}

}
