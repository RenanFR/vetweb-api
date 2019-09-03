package com.vetweb.api.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.hateoas.ResourceSupport;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "tbl_species")
@ApiModel(value = "Represents every kind of animal species that can be received in the clinic")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Species extends ResourceSupport implements ClinicEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@JsonProperty("id")
	private Long id;

	@Getter
	@Setter
	@NotNull
	@NotBlank
	private String description;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "species")
	private List<Animal> animalsOf;

	@Override
	public Long getEntityId() {
		return id;
	}

}
