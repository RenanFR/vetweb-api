package com.vetweb.api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vetweb.api.model.MedicalRecord;
import com.vetweb.api.persist.MedicalRecordRepository;

@Service
public class MedicalRecordService {
	
	@Autowired
	private MedicalRecordRepository repository;
	
	public MedicalRecord create(MedicalRecord medicalRecord) {
		return repository.save(medicalRecord);
	}

}
