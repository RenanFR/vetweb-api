package com.vetweb.api.config.batch;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import com.vetweb.api.model.PetOwner;
import com.vetweb.api.model.h2.BirthdayBoy;

@Configuration
@PropertySource("classpath:sql.properties")
public class BirthdayJob {
	
	@Autowired
	private JobBuilderFactory jobFactory;
	
	@Autowired
	private StepBuilderFactory stepFactory;
	
	@Bean
	public ItemReader<PetOwner> reader(@Qualifier("pg-datasource") DataSource dataSource, 
			@Value("${statements.queries.birthday}") String queryBirthday) {
		JdbcCursorItemReader<PetOwner> reader = new JdbcCursorItemReader<>();
		reader.setDataSource(dataSource);
		reader.setSql(queryBirthday.replace("?", String.valueOf(1)));
		reader.setRowMapper(new PetOwnerRowMapper());
		return reader;
	}
	
	@Bean
	public ItemProcessor<PetOwner, BirthdayBoy> processor() {
		return new BirthdayProcessor();
	}
	
	@Bean
	public ItemWriter<BirthdayBoy> writer() {
		return new BirthdayWriter();
	}
	
	@Bean(name = "step1")
	public Step step1(ItemReader<PetOwner> reader, ItemProcessor<PetOwner, BirthdayBoy> processor, ItemWriter<BirthdayBoy> writer) {
		return stepFactory.get("step1").<PetOwner, BirthdayBoy>chunk(100).reader(reader).processor(processor).writer(writer).build();
	}
	
	@Bean(name = "jobBirthday")
	public Job job(@Qualifier("step1") Step step) {
		return jobFactory.get("jobBirthday").start(step).build();
	}
	
}
