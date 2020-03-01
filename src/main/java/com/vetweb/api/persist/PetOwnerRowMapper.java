package com.vetweb.api.persist;

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
		return new PetOwner
				.Builder()
				.withCpf(rs.getString("cpf"))
				.havingFirstName(rs.getString("first_name"))
				.havingLastName(rs.getString("last_name"))
				.withDateBorn(rs.getDate("date_born").toLocalDate())
				.withGender(rs.getString("gender").charAt(0))
				.addContact(new ContactInfo(rs.getString("phone"), rs.getString("cell_phone"), rs.getString("email")))
				.addAddress(new Address(new AddressPrimaryKey(rs.getString("zip_code"), rs.getLong("num")), 
						rs.getString("street"), rs.getString("district"), rs.getString("city"), rs.getString("state"), rs.getString("country"), null))
				.includedAt(rs.getDate("date_inclusion").toLocalDate())
				.build();
	}

}
