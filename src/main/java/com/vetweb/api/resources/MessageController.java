package com.vetweb.api.resources;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.vetweb.api.model.mongo.Message;

@Controller
@RequestMapping("messages")
public class MessageController {
	
	@MessageMapping("message.send")
	@SendTo("/topic/test")
	public Message send(@Payload Message message) {
		System.out.println("Message received from client " + message);
		return message;
	}

}
