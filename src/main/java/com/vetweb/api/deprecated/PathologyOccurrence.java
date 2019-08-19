package com.vetweb.api.deprecated;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.vetweb.api.model.MedicalOccurrence;
import com.vetweb.api.model.MedicalRecord;
import com.vetweb.api.model.OccurrenceType;
import com.vetweb.api.model.Pathology;

import lombok.Data;
import lombok.EqualsAndHashCode;

//@Entity
//@Table(name = "tbl_pathology_occurrence")
//@Data
//@EqualsAndHashCode(callSuper=false)
public class PathologyOccurrence extends MedicalOccurrence {
	
	private MedicalRecord medicalRecord;
	
	private Pathology pathology;
	
	@Override
	public OccurrenceType getType() {
		return OccurrenceType.PATHOLOGY;
	}	

}