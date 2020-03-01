package com.vetweb.api.config.json;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.module.SimpleModule;
import com.vetweb.api.model.PetOwner;

@Service
public class PetOwnerJsonModule extends SimpleModule {

	private static final long serialVersionUID = 1L;
	
	public PetOwnerJsonModule() {
		this.addDeserializer(PetOwner.class, new PetOwnerDeserializer());
	}
	
	
}
