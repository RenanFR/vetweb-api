package com.vetweb.api.bean;

import java.util.Properties;

import org.springframework.context.annotation.Bean;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

public class MailConfig {
	
	@Bean
	public JavaMailSender mailSender() {
		
	    JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
	    mailSender.setHost("smtp.gmail.com");
	    mailSender.setPort(587);
	    mailSender.setUsername("springbootalura@gmail.com");
	    mailSender.setPassword("springboot");
	    
	    Properties properties = mailSender.getJavaMailProperties();
	    properties.put("mail.transport.protocol", "smtp");
	    properties.put("mail.smtp.auth", "true");
	    properties.put("mail.smtp.starttls.enable", "true");
	    properties.put("mail.debug", "true");
	    
	    return mailSender;
	    
	}	

}
