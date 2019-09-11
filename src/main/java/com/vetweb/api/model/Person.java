package com.vetweb.api.model;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Data
public class Person {
	
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE)
	private Long id;	
	
	private String cpf;

	@Column(name = "first_name")
	private String firstName;
	
	@Column(name = "last_name")
	private String lastName;
	
	@Transient
	@Getter(AccessLevel.NONE)
	@Setter(AccessLevel.NONE)
	private String fullName;
	
	@ManyToOne
	@JsonBackReference
	@JoinColumns({
		@JoinColumn(name = "zip_code", referencedColumnName = "zip_code"),
		@JoinColumn(name = "num", referencedColumnName = "num")
	})
	private Address address;
	
	private char gender;
	
	private String nationality;
	
	@Column(name = "date_born")
	private LocalDate dateBorn;
	
	private ContactInfo contactInfo;

	public String getFullName() {
		return firstName + " " + lastName;
	}

}
