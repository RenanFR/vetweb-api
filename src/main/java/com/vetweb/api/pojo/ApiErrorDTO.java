package com.vetweb.api.pojo;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ApiErrorDTO {
	
	private int status;
	
	private String errorMessage;
	
	private List<String> errors = new ArrayList<>();

}
