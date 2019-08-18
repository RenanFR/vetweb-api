package com.vetweb.api.deprecated;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.vetweb.api.model.Exam;
import com.vetweb.api.model.MedicalOccurrence;
import com.vetweb.api.model.MedicalRecord;
import com.vetweb.api.model.OccurrenceType;

import lombok.Data;
import lombok.EqualsAndHashCode;

//@Entity
//@Table(name = "tbl_exam_occurrence")
//@Data
//@EqualsAndHashCode(callSuper=false)
public class ExamOccurrence extends MedicalOccurrence {
	
	private MedicalRecord medicalRecord;
	
	private Exam exam;
	
	@Override
	public OccurrenceType getType() {
		return OccurrenceType.EXAM;
	}		

}
