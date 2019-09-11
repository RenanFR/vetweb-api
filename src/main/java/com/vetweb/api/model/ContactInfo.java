package com.vetweb.api.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Embeddable
@Getter
@Setter
@AllArgsConstructor
public class ContactInfo {
	
	private String phone;
	
	@Column(name = "cell_phone")
	private String cellPhone;
	
	private String email;

}
