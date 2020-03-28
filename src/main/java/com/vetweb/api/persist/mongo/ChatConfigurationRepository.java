package com.vetweb.api.persist.mongo;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.vetweb.api.model.mongo.ChatConfiguration;

public interface ChatConfigurationRepository extends MongoRepository<ChatConfiguration, String> {
	
	ChatConfiguration findByUserUserEmail(String userEmail);

}
