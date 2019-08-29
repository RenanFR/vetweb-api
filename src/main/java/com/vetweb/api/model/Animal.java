package com.vetweb.api.model;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Entity
@Table(name = "tbl_animal")
@Data
@ApiModel(description = "The main entity model of the system, represents a pet of any species")
public class Animal {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ApiModelProperty(notes = "Pet name selected by the owner", required = true)
	@Column(name = "nick_name")
	private String nickName;
	
	@Column(name = "date_born")
	private LocalDate dateBorn;
	
	private boolean alive;
	
	private Double weight;
	
	@ManyToOne
	@JoinColumn(name = "species_id", referencedColumnName = "id")
	private Species species;
	
	@ManyToOne
	@JoinColumn(name = "owner_id", referencedColumnName = "cpf")
	private PetOwner petOwner;
	
	@OneToOne(mappedBy = "animal")
	@JsonBackReference("animal")
	private MedicalRecord medicalRecord;
	
	@Column(name = "image_file")
	private String imageFile;

}
