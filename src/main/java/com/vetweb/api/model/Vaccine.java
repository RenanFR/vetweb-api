package com.vetweb.api.model;

import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "tbl_vaccine")
@Getter
@Setter
@EqualsAndHashCode(callSuper=false)
public class Vaccine extends ClinicService {
	
	private String group;
	
	private String laboratory;
	
}
