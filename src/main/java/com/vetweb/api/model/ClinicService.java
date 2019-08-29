package com.vetweb.api.model;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.Data;

@Entity
@Table(name = "tbl_clinic_service")
@Inheritance(strategy = InheritanceType.JOINED)
@Data
public class ClinicService {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String name;
	
	private String description;
	
	private boolean active;
	
	private BigDecimal price;
	
	@OneToMany(mappedBy = "clinicService")
	@JsonBackReference("clinicService")
	private List<MedicalOccurrence> occurrences;	

}
