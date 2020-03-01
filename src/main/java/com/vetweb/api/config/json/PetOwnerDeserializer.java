package com.vetweb.api.config.json;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.vetweb.api.model.Address;
import com.vetweb.api.model.AddressPrimaryKey;
import com.vetweb.api.model.ContactInfo;
import com.vetweb.api.model.PetOwner;

public class PetOwnerDeserializer extends JsonDeserializer<PetOwner> {

	@Override
	public PetOwner deserialize(JsonParser json, DeserializationContext ctxt) throws IOException, JsonProcessingException {
		ObjectCodec codec = json.getCodec();
		JsonNode jsonNode = codec.readTree(json);
		return new PetOwner.Builder()
				.havingFirstName(jsonNode.get("firstName").asText())
				.havingLastName(jsonNode.get("lastName").asText())
				.withCpf(jsonNode.get("cpf").asText())
				.withGender(jsonNode.get("gender").asText().toUpperCase().charAt(0))
				.withDateBorn(LocalDate.parse(jsonNode.get("dateBorn").asText(), DateTimeFormatter.ofPattern("ddMMyyyy")))
				.addAddress(new Address
						(new AddressPrimaryKey(jsonNode.get("address").get("zipCode").asText(), jsonNode.get("address").get("number").asLong()), 
						jsonNode.get("address").get("street").asText(), jsonNode.get("address").get("district").asText(), 
						jsonNode.get("address").get("city").asText(), jsonNode.get("address").get("state").asText(), null, null))
				.addContact(new ContactInfo(jsonNode.get("contactInfo").get("phone").asText(), jsonNode.get("contactInfo").get("cellPhone").asText(), jsonNode.get("contactInfo").get("email").asText()))
				.build();
	}

}
