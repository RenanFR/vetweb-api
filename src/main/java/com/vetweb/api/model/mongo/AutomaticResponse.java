package com.vetweb.api.model.mongo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AutomaticResponse {
	
	private boolean enabled;
	
	private String text;
	
	private Availability availability;
	
}
