package com.vetweb.api.config;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import com.vetweb.api.model.mongo.Availability;
import com.vetweb.api.model.mongo.ChatConfiguration;
import com.vetweb.api.model.mongo.Message;
import com.vetweb.api.model.mongo.WeekDay;
import com.vetweb.api.persist.mongo.ChatConfigurationRepository;
import com.vetweb.api.service.MessageService;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class KafkaMessageListener {
	
	@Autowired
	private SimpMessagingTemplate messagingTemplate;
	
	@Autowired
	private ChatConfigurationRepository configurationRepository;
	
	@Autowired
	private MessageService service;
	
	private static final String WEB_SOCKET_TOPIC = "/topic/chat/";
	
	@KafkaListener(topicPattern = "MESSAGE.VETWEB.*")
	public void handleMessageSent(@Payload Message message, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
		log.info("Received message {} from topic {}, sender {} to receiver {} at {}", message.getContent(), topic, message.getSender(), message.getReceiver(), message.getSentAt());
		ChatConfiguration configuration = configurationRepository.findByUserId(message.getIdReceiver());
		String receiverAvailability = checkReceiverAvailability(configuration);
		if (receiverAvailability != null) {
			Message automaticResponse = new Message();
			automaticResponse.setContent(receiverAvailability);
			automaticResponse.setSender(message.getReceiver());
			automaticResponse.setReceiver(message.getSender());
			automaticResponse.setIdSender(message.getIdReceiver());
			automaticResponse.setIdReceiver(message.getIdSender());
			automaticResponse.setSentAt(LocalDateTime.now());
			service.send(automaticResponse);
		}
		messagingTemplate.convertAndSend((WEB_SOCKET_TOPIC + message.getIdReceiver()), message);
	}
	
	public static String checkReceiverAvailability(ChatConfiguration configuration) {
		boolean available = false;
		Availability availability = configuration.getAutomaticResponse().getAvailability();
		List<WeekDay> days = List.of(availability.getSunday(), availability.getMonday(), availability.getTuesday(), availability.getWednesday(), availability.getThursday(), availability.getFriday(), availability.getSaturday());
		LocalDateTime now = LocalDateTime.now();
		WeekDay today = days.stream().filter(day -> day.getDayOfWeek().equals(now.getDayOfWeek())).findFirst().get();
		if (today.isEnabled() && !(now.toLocalTime().isBefore(today.getStartTime()) || now.toLocalTime().isAfter(today.getEndTime()))) {
			available = true;
		}
		if (!available) {
			return availability.getText();
		}
		return null;
	}
	

}
