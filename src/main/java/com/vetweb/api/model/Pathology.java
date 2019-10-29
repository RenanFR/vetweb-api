package com.vetweb.api.model;

import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.EqualsAndHashCode;

@Entity
@Table(name = "tbl_pathology")
@EqualsAndHashCode(callSuper=false)
public class Pathology extends ClinicService {
	
}
