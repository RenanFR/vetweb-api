package com.vetweb.api.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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
	
	public Message send(Message msg) {
		User userReceiver = userService.findByEmail(msg.getReceiver());
		kafkaTemplate.send("MESSAGE.VETWEB." + userReceiver.getId(), msg);
		Message saved = repository.save(msg);
		return saved;
	}
	
	public List<Message> messagesFromUser(String user) {
		return repository.findBySenderOrReceiver(user, user);
	}

}
