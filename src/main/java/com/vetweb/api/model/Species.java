package com.vetweb.api.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.springframework.hateoas.ResourceSupport;

import com.fasterxml.jackson.annotation.JsonBackReference;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "tbl_species")
@ApiModel(value = "Represents every kind of animal species that can be received in the clinic")
public class Species extends ResourceSupport implements ClinicEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Getter
	@Setter
	private String description;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "species")
	@JsonBackReference
	private List<Animal> animalsOf;

	@Override
	public Long getEntityId() {
		return id;
	}

}
