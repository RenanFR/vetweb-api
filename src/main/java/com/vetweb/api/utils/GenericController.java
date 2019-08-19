package com.vetweb.api.utils;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.vetweb.api.model.ClinicEntity;

public interface GenericController<E extends ClinicEntity> {
	
	ResponseEntity<E> searchById(Long id);
	
	ResponseEntity<List<E>> searchAll();

}
