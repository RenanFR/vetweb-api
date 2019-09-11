package com.vetweb.api.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.Data;

@Embeddable
@Data
@AllArgsConstructor
public class AddressPrimaryKey implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Column(name = "zip_code")
	private String zipCode;
	
	@Column(name = "num")
	private Long number;

}
