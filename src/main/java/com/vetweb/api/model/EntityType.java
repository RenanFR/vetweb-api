package com.vetweb.api.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum EntityType {
	
	SPECIES("species"),
	PET_OWNER("pet owners");
	
	@Getter
	private final String description;
	
}
