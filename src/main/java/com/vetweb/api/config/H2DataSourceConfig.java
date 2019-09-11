package com.vetweb.api.config;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.quartz.QuartzDataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
  entityManagerFactoryRef = "h2-entity-manager-factory",
  transactionManagerRef = "h2-tx-manager",
  basePackages = { "com.vetweb.api.persist.h2" }
)
@PropertySource("quartz.properties")
public class H2DataSourceConfig {
	
	@Bean(name = "h2-datasource")
	@QuartzDataSource
	@ConfigurationProperties(prefix = "spring.datasource.h2")
	public DataSource dataSource() {
		return DataSourceBuilder.create().build();
	}
	
	@Bean(name = "h2-entity-manager-factory")
	public LocalContainerEntityManagerFactoryBean entityManagerFactory(
		EntityManagerFactoryBuilder builder,
		@Qualifier("h2-datasource") DataSource dataSource
	) {
		Map<String, String> properties = new HashMap<>();
		properties.put("hibernate.dialect", "org.hibernate.dialect.H2Dialect");
		properties.put("hibernate.hbm2ddl.auto", "create-drop");
		return
			builder
				.dataSource(dataSource)
			    .packages("com.vetweb.api.model.h2")
			    .properties(properties)
			    .persistenceUnit("vetweb-h2")
			    .build();
	}	
	  
	@Bean(name = "h2-tx-manager")
	public PlatformTransactionManager transactionManager(
		@Qualifier("h2-entity-manager-factory") EntityManagerFactory factory
	) {
	    return new JpaTransactionManager(factory);
	}	
	  
	@Bean(name = "h2-jdbc-template")
	public JdbcTemplate jdbcTemplate(@Qualifier("h2-datasource") DataSource dataSource) {
		return new JdbcTemplate(dataSource);
	}	  
	
}
