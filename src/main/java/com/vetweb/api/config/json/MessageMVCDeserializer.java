package com.vetweb.api.config.json;

import java.io.IOException;
import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.vetweb.api.model.mongo.Message;

@Component
public class MessageMVCDeserializer extends JsonDeserializer<Message> {
	
	@Override
	public Message deserialize(JsonParser json, DeserializationContext ctxt) throws IOException, JsonProcessingException {
		Message message = new Message();
		JsonNode jsonNode = json.getCodec().readTree(json);
		message.setContent(jsonNode.get("text").asText());
		message.setSender(jsonNode.get("sender").asText());
		message.setIdSender(jsonNode.get("idSender").asLong());
		message.setReceiver(jsonNode.get("receiver").asText());
		message.setIdReceiver(jsonNode.get("idReceiver").asLong());
		message.setSentAt(LocalDateTime.now());
		return message;
	}

}
