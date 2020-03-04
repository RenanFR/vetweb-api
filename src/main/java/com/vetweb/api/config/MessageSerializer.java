package com.vetweb.api.config;

import java.util.Map;

import org.apache.kafka.common.serialization.Serializer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vetweb.api.model.mongo.Message;

public class MessageSerializer implements Serializer<Message> {

	@Override
	public void configure(Map<String, ?> configs, boolean isKey) {
	}

	@Override
	public byte[] serialize(String topic, Message data) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			return mapper.writeValueAsBytes(data);
		} catch (JsonProcessingException exception) {
			exception.printStackTrace();
		}
		return null;
	}

	@Override
	public void close() {
	}

}
