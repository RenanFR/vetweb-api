package com.vetweb.api.config.scheduling;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

import java.io.IOException;
import java.util.List;
import java.util.Properties;

import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.scheduling.quartz.SpringBeanJobFactory;

import com.vetweb.api.jobs.BirthdayJobTrigger;
import com.vetweb.api.jobs.HelloWorldJob;

@Configuration
public class QuartzConfig {
	
	@Autowired
	private ApplicationContext applicationContext;
	
	@Bean
	public JobDetail jobHelloWorld() {
		return newJob(HelloWorldJob.class).storeDurably().withIdentity("job-hello-world").withDescription("Sample Hello World job to test configuration").build();
	}
	
	@Bean
	public JobDetail birthdayJobTrg() {
		return newJob(BirthdayJobTrigger.class).storeDurably().withIdentity("job-birthday").withDescription("Job to notify congratulations message on birthday").build();
	}
	
	@Bean
	public Trigger triggerHelloWorld(@Qualifier("jobHelloWorld") JobDetail job) {
		return newTrigger().forJob(job).withIdentity("trg-hello-world").withDescription("Trigger to first Hello World job").withSchedule(simpleSchedule().withIntervalInSeconds(10).repeatForever()).build();
	}
	
	@Bean
	public Trigger triggerBirthday(@Qualifier("birthdayJobTrg") JobDetail job) {
		return newTrigger().forJob(job).withIdentity("trg-birthday").withDescription("Trigger to birthday job").withSchedule(simpleSchedule().withIntervalInMinutes(10).repeatForever()).build();
	}
	
	@Bean
	@Autowired
	public Scheduler scheduler(List<Trigger> triggers, List<JobDetail> jobs, SchedulerFactoryBean schedulerFactory) throws SchedulerException {
		Scheduler scheduler = schedulerFactory.getScheduler();
		for (JobDetail j : jobs) {
			scheduler.scheduleJob(j, triggers.stream().filter(trg -> trg.getJobKey().equals(j.getKey())).findAny().get());
		}
		scheduler.start();
		return scheduler;
	}
	
	@Bean
	public SpringBeanJobFactory jobFactory() {
		AutoWiringSpringBeanJobFactory springJobFactory = new AutoWiringSpringBeanJobFactory();
		springJobFactory.setContext(applicationContext);
		return springJobFactory;
	}
	
	private Properties quartzProperties() throws IOException {
		PropertiesFactoryBean propertiesFactory = new PropertiesFactoryBean();
		propertiesFactory.setLocation(new ClassPathResource("/quartz.properties"));
		propertiesFactory.afterPropertiesSet();
		return propertiesFactory.getObject();
	}
	
	@Bean
	public SchedulerFactoryBean schedulerFactory(SpringBeanJobFactory jobFactory) throws IOException {
		SchedulerFactoryBean factory = new SchedulerFactoryBean();
		factory.setJobFactory(jobFactory);
		factory.setQuartzProperties(quartzProperties());
		return factory;
	}

}
