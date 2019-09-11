package com.vetweb.api.model;

import java.util.List;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.AllArgsConstructor;
import lombok.Data;

@Entity
@Table(name = "tbl_address")
@Data
@AllArgsConstructor
public class Address {
	
	@EmbeddedId
	private AddressPrimaryKey pk;
	
	public Address(AddressPrimaryKey pk) {
		this.pk = pk;
	}

	private String street;
	
	private String district;
	
	private String city;
	
	private String state;
	
	private String country;
	
	@OneToMany(mappedBy = "address")
	@JsonManagedReference
	private List<Person> person;

}
