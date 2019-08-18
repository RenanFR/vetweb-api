package com.vetweb.api.deprecated;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.vetweb.api.model.Consultation;
import com.vetweb.api.model.MedicalOccurrence;
import com.vetweb.api.model.MedicalRecord;
import com.vetweb.api.model.OccurrenceType;

import lombok.Data;
import lombok.EqualsAndHashCode;

//@Entity
//@Table(name = "tbl_consultation_occurrence")
//@Data
//@EqualsAndHashCode(callSuper=false)
public class ConsultationOccurrence extends MedicalOccurrence {
	
	private MedicalRecord medicalRecord;
	
	private Consultation consultation;
	
	@Override
	public OccurrenceType getType() {
		return OccurrenceType.CONSULTATION;
	}	

}
