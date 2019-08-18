package com.vetweb.api.model;

import java.time.Duration;
import java.time.Period;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.vetweb.api.utils.FrequencyConverter;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "tbl_consultation")
@Data
@EqualsAndHashCode(callSuper=false)
public class Consultation extends ClinicService {
	
	@Column(name = "time_spent")
	private Duration timeSpent;
	
	@Convert(converter = FrequencyConverter.class)
	private Period frequency;
	
	@Column(name = "form_model")
	private String formModel;

}
