package com.vetweb.api.deprecated;

import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;

import com.vetweb.api.model.PetOwner;

public class BirthdayJob {
	
	@Autowired
	private JobBuilderFactory jobFactory;
	
	@Autowired
	private StepBuilderFactory stepFactory;
	
	@Bean
	public ItemReader<PetOwner> reader() {
		return new OwnersReader();
	}
	
}
