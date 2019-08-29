package com.vetweb.api.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "tbl_scheduling")
@Data
public class Scheduling {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne
	@JoinColumn(name = "occurrence_id", referencedColumnName = "id")
	private MedicalOccurrence occurrence;
	
	@Column(name = "initial_dt")
	private LocalDateTime initialDT;
	
	@Column(name = "final_dt")
	private LocalDateTime finalDT;

}
