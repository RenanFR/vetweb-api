package com.vetweb.api.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.vetweb.api.model.auth.User;
import com.vetweb.api.model.mongo.Message;
import com.vetweb.api.persist.mongo.MessageRepository;
import com.vetweb.api.service.auth.UserService;

@Service
public class MessageService {
	
	@Autowired
	private KafkaTemplate<String, Message> kafkaTemplate;
	
	@Autowired
	private MessageRepository repository;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private MongoOperations mongoOperations;
	
	public Message send(Message msg) {
		User userReceiver = userService.findByEmail(msg.getReceiver());
		kafkaTemplate.send("MESSAGE.VETWEB." + userReceiver.getId(), msg);
		Message saved = repository.save(msg);
		return saved;
	}
	
	public List<Message> messagesFromUser(String user) {
		return repository.findBySenderOrReceiver(user, user);
	}
	
	public List<Message> findMessagesBySearchTerm(String searchTerm) {
		Query query = new Query();
		query.addCriteria(Criteria.where("content").regex(searchTerm));
		List<Message> messagesContaining = mongoOperations.find(query, Message.class);
		return messagesContaining;
	}
	
}
