package com.vetweb.api.resources;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vetweb.api.model.mongo.Message;
import com.vetweb.api.service.MessageService;

@RestController
@RequestMapping("chat")
public class ChatResource {
	
	@Autowired
	private MessageService service;
	
	@PostMapping
	public ResponseEntity<Message> send(@RequestBody Message msg) {
		Message sent = service.send(msg);
		return ResponseEntity.ok(sent);
	}
	
	@GetMapping("{user}")
	public ResponseEntity<List<Message>> messagesFromUser(@PathVariable("user") String user) {
		List<Message> messages = service.messagesFromUser(user);
		return ResponseEntity.ok(messages);
	}

}
