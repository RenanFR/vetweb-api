package com.vetweb.api.persist;


import java.time.LocalDate;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.vetweb.api.model.PetOwner;

import lombok.extern.slf4j.Slf4j;

@Repository
@PropertySource("sql.properties")
@Slf4j
public class PetOwnerRepository {
	
	@Autowired
	@Qualifier("pg-jdbc-template")
	private JdbcTemplate jdbcTemplate;
	
	private static String INSERT_ADDRESS;
	
	private static String INSERT_OWNER;
	
	private static String QRY_COUNT;

	@Value("${statements.insert.address}")
	public static void setInsertAddress(String insertAddress) {
		INSERT_ADDRESS = insertAddress;
	}

	@Value("${statements.insert.owner}")
	public static void setInsertOwner(String insertOwner) {
		INSERT_OWNER = insertOwner;
	}
	
	@Value("${statements.queries.count}")
	public static void setQueryCount(String queryCount) {
		QRY_COUNT = queryCount;
	}
	
	public Integer create(PetOwner petOwner) {
		jdbcTemplate.update(INSERT_ADDRESS, new Object[ ] {
				petOwner.getAddress().getPk().getZipCode(), petOwner.getAddress().getPk().getNumber(), petOwner.getAddress().getStreet(),
				petOwner.getAddress().getDistrict(), petOwner.getAddress().getCity(), petOwner.getAddress().getState(), petOwner.getAddress().getCountry()
		});
		jdbcTemplate.update(INSERT_OWNER, new Object[ ] {
			petOwner.getCpf(), LocalDate.now(), petOwner.getFirstName(), petOwner.getLastName(), petOwner.getGender(), petOwner.getNationality(),
			petOwner.getDateBorn(), petOwner.getContactInfo().getPhone(), petOwner.getContactInfo().getCellPhone(), petOwner.getContactInfo().getEmail(),
			petOwner.getAddress().getPk().getZipCode(), petOwner.getAddress().getPk().getNumber() 
		});
		return jdbcTemplate.queryForObject(QRY_COUNT, Integer.class);
	}

}
