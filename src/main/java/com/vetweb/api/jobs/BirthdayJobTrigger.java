package com.vetweb.api.jobs;

import java.util.Date;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BirthdayJobTrigger implements Job {
	
	@Autowired
	private JobLauncher launcher;
	
	@Autowired
	@Qualifier("jobBirthday")
	private org.springframework.batch.core.Job job;

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		try {
			launcher.run(job, new JobParametersBuilder().addDate("executionTime", new Date()).toJobParameters());
		} catch (Exception e) {
			log.error(e.getMessage());
		}
	}

}
