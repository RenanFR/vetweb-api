package com.vetweb.api.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.Data;

@Entity
@Table(name = "tbl_medical_record")
@Data
public class MedicalRecord {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id; 
	
	@OneToOne
	@JsonManagedReference("animal")
	@JoinColumn(name = "animal_id", referencedColumnName = "id")
	private Animal animal;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "medicalRecord")
	@JsonIgnore
	private List<MedicalOccurrence> occurrences;

}
