package com.vetweb.api.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import com.vetweb.api.model.mongo.Message;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class KafkaMessageListener {
	
	@Autowired
	private SimpMessagingTemplate messagingTemplate;
	
	@KafkaListener(topicPattern = "MESSAGE.VETWEB.*")
	public void handleMessageSent(@Payload Message message, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
		log.info("Received message {} from topic {}, sender {} to receiver {} at {}", message.getContent(), topic, message.getSender(), message.getReceiver(), message.getSentAt());
		messagingTemplate.convertAndSend(("/topic/chat/" + message.getIdReceiver()), message);
	}
	

}
