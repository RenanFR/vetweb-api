package com.vetweb.api.model;

import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "tbl_vaccine")
@Data
@EqualsAndHashCode(callSuper=false)
public class Vaccine extends ClinicService {
	
	private String group;
	
	private String laboratory;
	
}
