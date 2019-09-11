package com.vetweb.api.model.h2;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "tbl_birthday")
@Getter
@Setter
@AllArgsConstructor
public class BirthdayBoy {
	
	@Id
	private Long id;
	
	@Column(name = "customer_name")
	private String customerName;
	
	@Column(name = "customer_email")
	private String customerEmail;
	
	@Column(name = "date_born")
	private LocalDate dateBorn;

}
