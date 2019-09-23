package com.vetweb.api.config.batch;

import javax.sql.DataSource;

import org.springframework.batch.core.configuration.annotation.BatchConfigurer;
import org.springframework.batch.core.configuration.annotation.DefaultBatchConfigurer;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.JobRepositoryFactoryBean;
import org.springframework.batch.support.transaction.ResourcelessTransactionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@EnableBatchProcessing
public class BatchConfig {
	
	@Value("classpath:/org/springframework/batch/core/schema-drop-h2.sql")
	private Resource sqlSchemaBatchDrop;
	
	@Value("classpath:/org/springframework/batch/core/schema-h2.sql")
	private Resource sqlSchemaBatchCreate;
	
	@Autowired
	@Qualifier("h2-datasource")
	private DataSource dataSource;
	
	@Bean
	public DataSourceInitializer initializer() {
		ResourceDatabasePopulator dbPopulator = new ResourceDatabasePopulator();
		dbPopulator.addScript(sqlSchemaBatchDrop);
		dbPopulator.addScript(sqlSchemaBatchCreate);
		dbPopulator.setIgnoreFailedDrops(true);
		DataSourceInitializer initializer = new DataSourceInitializer();
		initializer.setDataSource(dataSource);
		initializer.setDatabasePopulator(dbPopulator);
		return initializer;
	}
	
	private JobRepository getJobRepository() throws Exception {
		JobRepositoryFactoryBean factory = new JobRepositoryFactoryBean();
		factory.setDataSource(dataSource);
		factory.setTransactionManager(getTransactionManager());
		factory.afterPropertiesSet();
		return (JobRepository) factory.getObject();
	}
	
	private PlatformTransactionManager getTransactionManager() {
		return new ResourcelessTransactionManager();
	}
	
	public JobLauncher getJobLauncher() throws Exception {
		SimpleJobLauncher jobLauncher = new SimpleJobLauncher();
		jobLauncher.setJobRepository(getJobRepository());
		jobLauncher.afterPropertiesSet();
		return jobLauncher;
	}
	
	@Bean
	public BatchConfigurer batchConfigurer() {
		return new DefaultBatchConfigurer(dataSource);
	}

}
