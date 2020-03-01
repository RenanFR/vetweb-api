package com.vetweb.api.persist;


import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.vetweb.api.model.PetOwner;

@Repository
@PropertySource("classpath:sql.properties")
public class PetOwnerRepository {
	
	@Autowired
	@Qualifier("pg-jdbc-template")
	private JdbcTemplate jdbcTemplate;
	
	private static String INSERT_ADDRESS;
	
	private static String INSERT_OWNER;
	
	private static String QRY_COUNT;

	@Value("${statements.insert.address}")
	public void setInsertAddress(String insertAddress) {
		INSERT_ADDRESS = insertAddress;
	}

	@Value("${statements.insert.owner}")
	public void setInsertOwner(String insertOwner) {
		INSERT_OWNER = insertOwner;
	}
	
	@Value("${statements.queries.count}")
	public void setQueryCount(String queryCount) {
		QRY_COUNT = queryCount;
	}
	
	public Integer create(PetOwner petOwner) {
		Integer existsAddress = jdbcTemplate.queryForObject("select count(*) from tbl_address where zip_code = '" + petOwner.getAddress().getPk().getZipCode() + "' and num = " + petOwner.getAddress().getPk().getNumber() + ";", Integer.class);
		if (existsAddress != 1)
			jdbcTemplate.update(INSERT_ADDRESS, new Object[ ] {
					petOwner.getAddress().getPk().getZipCode(), petOwner.getAddress().getPk().getNumber(), petOwner.getAddress().getStreet(),
					petOwner.getAddress().getDistrict(), petOwner.getAddress().getCity(), petOwner.getAddress().getState(), petOwner.getAddress().getCountry()
			});
		jdbcTemplate.update(INSERT_OWNER, new Object[ ] {
			petOwner.getCpf(), LocalDate.now(), petOwner.getFirstName(), petOwner.getLastName(), petOwner.getGender(), null,
			petOwner.getDateBorn(), petOwner.getContactInfo().getPhone(), petOwner.getContactInfo().getCellPhone(), petOwner.getContactInfo().getEmail(),
			petOwner.getAddress().getPk().getZipCode(), petOwner.getAddress().getPk().getNumber() 
		});
		return jdbcTemplate.queryForObject(QRY_COUNT, Integer.class);
	}
	
	public PetOwner searchById(Long id) {
		return jdbcTemplate.queryForObject("SELECT * FROM tbl_pet_owner po JOIN tbl_address ad ON po.zip_code = ad.zip_code AND po.num = ad.num  WHERE id = ?;", 
				new PetOwnerRowMapper(), 
				new Object[ ] { id });
	}
	
	public List<PetOwner> searchAll() {
		return jdbcTemplate.query("SELECT * FROM tbl_pet_owner po JOIN tbl_address ad ON po.zip_code = ad.zip_code AND po.num = ad.num;", 
				new PetOwnerRowMapper());
	}

}
