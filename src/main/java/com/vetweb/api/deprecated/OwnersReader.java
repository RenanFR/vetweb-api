package com.vetweb.api.deprecated;

import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;

import com.vetweb.api.model.PetOwner;

public class OwnersReader implements ItemReader<PetOwner> {

	@Override
	public PetOwner read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
		// TODO Auto-generated method stub
		return null;
	}

}
