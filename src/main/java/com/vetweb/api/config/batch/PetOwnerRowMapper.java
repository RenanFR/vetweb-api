package com.vetweb.api.config.batch;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.vetweb.api.model.Address;
import com.vetweb.api.model.AddressPrimaryKey;
import com.vetweb.api.model.ContactInfo;
import com.vetweb.api.model.PetOwner;

public class PetOwnerRowMapper implements RowMapper<PetOwner> {

	@Override
	public PetOwner mapRow(ResultSet rs, int rowNum) throws SQLException {
		PetOwner petOwner = new PetOwner.Builder()
				.withCpf(rs.getString("cpf"))
				.havingFirstName(rs.getString("first_name"))
				.havingLastName(rs.getString("last_name"))
				.withGender(rs.getString("gender").toCharArray()[0])
				.withDateBorn(rs.getDate("date_born").toLocalDate())
				.includedAt(rs.getDate("date_inclusion").toLocalDate())
				.addAddress(new Address(new AddressPrimaryKey(rs.getString("zip_code"), rs.getLong("num"))))
				.addContact(new ContactInfo(rs.getString("phone"), rs.getString("cell_phone"), rs.getString("email")))
				.build();
		petOwner.setId(rs.getLong("id"));
		return petOwner;
	}

}
