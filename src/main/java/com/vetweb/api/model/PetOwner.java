package com.vetweb.api.model;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "tbl_pet_owner")
@Data
@EqualsAndHashCode(callSuper=false)
public class PetOwner extends Person implements ClinicEntity {
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "petOwner")
	@JsonIgnore
	private List<Animal> animalsOwned;
	
	@Column(name = "date_inclusion")
	private LocalDate dateInclusion;
	
	private boolean active;
	
	private String profession;
	
	@Transient
	private Integer age;

	@Override
	public Long getEntityId() {
		return getId();
	}
	
	private PetOwner(Builder builder) {
		this.setCpf(builder.cpf);
		this.setFirstName(builder.firstName);
		this.setLastName(builder.lastName);
		this.setAddress(builder.address);
		this.setGender(builder.gender);
		this.setNationality(builder.nationality);
		this.setDateBorn(builder.dateBorn);
		this.setContactInfo(builder.contactInfo);
		this.setDateInclusion(builder.dateInclusion);
		this.setActive(builder.active);
		this.setProfession(builder.profession);
	}
	
	public Integer getAge() {
		return (int)ChronoUnit.YEARS.between(getDateBorn(), LocalDate.now());
	}

	public static class Builder {

		private String cpf;
		private String firstName;
		private String lastName;
		private Address address;
		private char gender;
		private String nationality;
		private LocalDate dateBorn;
		private ContactInfo contactInfo;		
		private LocalDate dateInclusion;
		private boolean active;
		private String profession;
		
		public Builder withCpf(String cpf) {
			this.cpf = cpf;
			return this;
		}
		
		public Builder havingFirstName(String firstName) {
			this.firstName = firstName;
			return this;
		}
		
		public Builder havingLastName(String lastName) {
			this.lastName = lastName;
			return this;
		}
		
		public Builder addAddress(Address address) {
			this.address = address;
			return this;
		}		
		
		public Builder withGender(char gender) {
			this.gender = gender;
			return this;
		}	
		
		public Builder fromCountry(String country) {
			this.nationality = country;
			return this;
		}
		
		public Builder withDateBorn(LocalDate dateBorn) {
			this.dateBorn = dateBorn;
			return this;
		}	
		
		public Builder addContact(ContactInfo contactInfo) {
			this.contactInfo = contactInfo;
			return this;
		}
		
		public Builder includedAt(LocalDate dateInclusion) {
			this.dateInclusion = dateInclusion;
			return this;
		}
		
		public Builder setActive(boolean active) {
			this.active = active;
			return this;
		}
		
		public Builder withProfession(String profession) {
			this.profession = profession;
			return this;
		}		
		
		public PetOwner buildForTest() {
			return new PetOwner.Builder()
					.withCpf("42649206802")
					.havingFirstName("Rafaela")
					.havingLastName("Moraes")
					.withDateBorn(LocalDate.of(1984, 6, 25))
					.includedAt(LocalDate.now())
					.withGender('f')
					.withProfession("Gastronomia")
					.setActive(true)
					.addAddress(new Address(new AddressPrimaryKey("11454350", (long)127), "Rua Suzano", "Vila Áurea (Vicente de Carvalho)", "Guarujá", "SP", "Brasil", Collections.emptyList()))
					.addContact(new ContactInfo("1327279442", "13988257952", "rafaelaflaviasophiamoraes..rafaelaflaviasophiamoraes@raioazul.com.br"))
					.build();
		}
		
		public PetOwner build() {
			return new PetOwner(this);
		}
		
	}
	
}
