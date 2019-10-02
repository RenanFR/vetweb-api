package com.vetweb.api.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import liquibase.configuration.GlobalConfiguration;
import liquibase.configuration.LiquibaseConfiguration;
import liquibase.integration.spring.SpringLiquibase;

@Configuration
public class LiquibaseConfig {
	
	@Autowired
	@Qualifier(value = "pg-datasource")
	private DataSource dataSource;
	
	@Value("${spring.liquibase.database-change-log-table}")
	private String changelogTable;

	@Value("${spring.liquibase.database-change-log-lock-table}")
	private String changelogLockTable;
	
	@Value("${spring.liquibase.change-log}")
	private String changelog;
	
	@Bean
	public SpringLiquibase liquibase() {
		GlobalConfiguration globalConfiguration = LiquibaseConfiguration.getInstance().getConfiguration(GlobalConfiguration.class);
		globalConfiguration.setDatabaseChangeLogTableName(changelogTable);
		globalConfiguration.setDatabaseChangeLogLockTableName(changelogLockTable);
		SpringLiquibase liquibase = new SpringLiquibase();
		liquibase.setDataSource(dataSource);
		liquibase.setChangeLog(changelog);
		return liquibase;
	}
	
}
