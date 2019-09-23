package com.vetweb.api.config.batch;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;

import com.vetweb.api.model.h2.BirthdayBoy;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@PropertySource("classpath:sql.properties")
public class BirthdayWriter implements ItemWriter<BirthdayBoy> {
	
	@Autowired
	@Qualifier("h2-jdbc-template")
	private JdbcTemplate jdbcTemplate;
	
	@Value("${statements.insert.birthday}")
	private String INSERT_BIRTHDAY;
	
	@Override
	public void write(List<? extends BirthdayBoy> items) throws Exception {
		log.info(String.format("Saving %d customers with birthday this month", items.size()));
		jdbcTemplate
			.batchUpdate(INSERT_BIRTHDAY, new BatchPreparedStatementSetter() {
				
				@Override
				public void setValues(PreparedStatement ps, int i) throws SQLException {
					BirthdayBoy birthdayBoy = items.get(i);
					ps.setLong(1, birthdayBoy.getId());
					ps.setString(2, birthdayBoy.getCustomerName());
					ps.setString(3, birthdayBoy.getCustomerEmail());
					ps.setDate(4, Date.valueOf(birthdayBoy.getDateBorn()));
				}
				
				@Override
				public int getBatchSize() {
					return items.size();
				}
			});
	}

}
