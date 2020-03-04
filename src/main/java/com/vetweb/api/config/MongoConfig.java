package com.vetweb.api.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;

import com.mongodb.MongoClient;

@Configuration
public class MongoConfig extends AbstractMongoConfiguration {

	@Override
	public MongoClient mongoClient() {
		return new MongoClient("127.0.0.1", 27017);
	}

	@Override
	public String getDatabaseName() {
		return "vetweb-mongo";
	}
	
	@Override
	public String getMappingBasePackage() {
		return "com.vetweb.api.model.mongo";
	}

}
