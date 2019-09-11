package com.vetweb.api.config.batch;

import java.time.LocalDate;

import org.springframework.batch.item.ItemProcessor;

import com.vetweb.api.model.PetOwner;
import com.vetweb.api.model.h2.BirthdayBoy;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BirthdayProcessor implements ItemProcessor<PetOwner, BirthdayBoy> {

	@Override
	public BirthdayBoy process(PetOwner petOwner) throws Exception {
		log.info(String.format("Customer %s is turning %d this %s month", petOwner.getFullName(), petOwner.getAge(), LocalDate.now().getMonth().toString()));
		return new BirthdayBoy(petOwner.getId(), petOwner.getFullName(), petOwner.getContactInfo().getEmail(), petOwner.getDateBorn());
	}

}
