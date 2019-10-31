package com.vetweb.api.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class ReportParameter {
	
	private String key;
	
	private ParameterType type;
	
	private Object value;
	
}
