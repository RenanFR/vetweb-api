package com.vetweb.api.model;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.ResourceSupport;

import com.fasterxml.jackson.annotation.JsonBackReference;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "tbl_animal")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ApiModel(description = "The main entity model of the system, represents a pet of any species")
public class Animal extends ResourceSupport implements ClinicEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ApiModelProperty(notes = "Pet name selected by the owner", required = true)
	@Column(name = "nick_name")
	private String nickName;
	
	@Column(name = "date_born")
	private LocalDate dateBorn;
	
	private boolean alive;
	
	private Double weight;
	
	@ManyToOne
	@JoinColumn(name = "species_id", referencedColumnName = "id")
	private Species species;
	
	@ManyToOne
	@JoinColumn(name = "owner_id", referencedColumnName = "id")
	private PetOwner petOwner;
	
	@OneToOne(mappedBy = "animal")
	@JsonBackReference("animal")
	private MedicalRecord medicalRecord;
	
	@Column(name = "image_file")
	private String imageFile;

	@Override
	public Link getId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Long getEntityId() {
		return id;
	}

}
