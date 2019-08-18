package com.vetweb.api.model;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "tbl_pet_owner")
@Data
@EqualsAndHashCode(callSuper=false)
public class PetOwner extends Person {
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "petOwner")
	@JsonBackReference
	private List<Animal> animalsOwned;
	
	@Column(name = "date_inclusion")
	private LocalDate dateInclusion;
	
	private boolean active;
	
	private String profession;
	
}
