package com.vetweb.api.deprecated;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.vetweb.api.model.MedicalOccurrence;
import com.vetweb.api.model.OccurrenceType;

import lombok.Data;
import lombok.EqualsAndHashCode;

//@Entity
//@Table(name = "tbl_vaccine_occurrence")
//@Data
//@EqualsAndHashCode(callSuper=false)
public class VaccineOccurrence extends MedicalOccurrence {
	
	@Override
	public OccurrenceType getType() {
		return OccurrenceType.VACCINE;
	}
	
}
