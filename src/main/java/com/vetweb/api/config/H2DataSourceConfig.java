package com.vetweb.api.config;

import javax.sql.DataSource;

import org.springframework.boot.autoconfigure.quartz.QuartzDataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@PropertySource("quartz.properties")
public class H2DataSourceConfig {
	
	@Bean(name = "h2-datasource")
	@QuartzDataSource
	@ConfigurationProperties(prefix = "spring.datasource.h2")
	public DataSource dataSource() {
		return DataSourceBuilder.create().build();
	}
	
}
