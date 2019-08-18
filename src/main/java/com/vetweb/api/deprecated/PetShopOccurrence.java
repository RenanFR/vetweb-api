package com.vetweb.api.deprecated;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.vetweb.api.model.MedicalOccurrence;
import com.vetweb.api.model.MedicalRecord;
import com.vetweb.api.model.OccurrenceType;
import com.vetweb.api.model.PetShop;

import lombok.Data;
import lombok.EqualsAndHashCode;

//@Entity
//@Table(name = "tbl_petshop_occurrence")
//@Data
//@EqualsAndHashCode(callSuper=false)
public class PetShopOccurrence extends MedicalOccurrence {
	
	private MedicalRecord medicalRecord;
	
	private PetShop petShop;
	
	@Override
	public OccurrenceType getType() {
		return OccurrenceType.PETSHOP;
	}		

}
