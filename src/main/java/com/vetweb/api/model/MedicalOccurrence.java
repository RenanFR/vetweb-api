package com.vetweb.api.model;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.Data;

@Entity
@Table(name = "tbl_medical_occurrence")
@Inheritance(strategy = InheritanceType.JOINED)
@Data
public abstract class MedicalOccurrence {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String description;
	
	@ManyToOne
	@JoinColumn(name = "medical_record_id", referencedColumnName = "id")
	private MedicalRecord medicalRecord;
	
	@OneToMany(mappedBy = "occurrence")
	@JsonIgnore
	private List<Scheduling> schedulings;
	
	@Enumerated(EnumType.STRING)
	private OccurrenceType type;
	
	@Column(name = "occurrence_date")
	private LocalDate occurrenceDate;
	
	private boolean paid;
	
	@ManyToOne
	@JsonManagedReference("clinicService")
	@JoinColumn(name = "service_id", referencedColumnName = "id")
	private ClinicService clinicService;

}
