package com.vetweb.api.persist.mongo;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.vetweb.api.model.mongo.Message;

public interface MessageRepository extends MongoRepository<Message, String> {
	
	List<Message> findBySenderOrReceiver(String sender, String receiver);

}
